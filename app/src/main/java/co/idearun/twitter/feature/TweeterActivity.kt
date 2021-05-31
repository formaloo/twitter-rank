package co.idearun.twitter.feature

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import co.idearun.twitter.BuildConfig
import co.idearun.twitter.R
import co.idearun.twitter.common.exception.Failure
import co.idearun.twitter.common.extension.NumberExt
import co.idearun.twitter.data.model.User
import co.idearun.twitter.databinding.ActivityTweeterBinding
import co.idearun.twitter.feature.adapter.FollowingsAdapter
import co.idearun.twitter.feature.viewmodel.*
import com.github.anastr.speedviewlib.components.Section
import com.github.anastr.speedviewlib.components.Style
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class TweeterActivity : BaseActivity(), UserListener {

    private lateinit var followingsAdapter: FollowingsAdapter
    lateinit var binding: ActivityTweeterBinding
    private var resume = true


    private var tweeterScore: Int? = null
    private var tweeterData: User? = null

    val twitterVM: TwitterViewModel by viewModel()
    val followingVM: CDPViewModel by viewModel()

    private val entries = ArrayList<PieEntry>()
    private val colors = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tweeter)
        binding.userContentLay.userHeaderLay.viewmodel = twitterVM
        binding.userContentLay.userHeaderLay.listener = this
        binding.userContentLay.viewmodel = twitterVM
        binding.sharePopUp.viewmodel = twitterVM
        binding.sharePopUp.listener = this

        binding.viewmodel = twitterVM
        binding.lifecycleOwner = this

        // CDP Version

        /**
         * In the mainActivity we searched/create a Tweeter and fetch his/her Score from CDP.
         * CDP give us the Tweeter personal score.
         * To calculate Tweeter total score we need his/her followingsScore, witch is the average of followings personal score
         *
         * FollowingsScore= [CDPViewModel.findFollowingsAvgScore(followings)]
         *
         *
         * Here after retrieve the Tweeter followingsList [twitterVM.getFollowings],
         * search each "[User.screen_name]@twitter.com" in CDP if user has already been saved just retrieve score
         * else create customer with user data
         * So totalScore= PersonalScore+ FollowingScore
         * */

        initView()
        checkBundle()
        initData()
    }

    private fun initView() {
        followingsAdapter = FollowingsAdapter(twitterVM, this@TweeterActivity)
        binding.userContentLay.userRv.apply {
            adapter = followingsAdapter
            layoutManager = LinearLayoutManager(this@TweeterActivity)
        }

    }

    private fun checkBundle() {
        intent.extras?.let { bundle ->
            bundle.getSerializable("tweeter")?.let { tweeterData ->
                tweeterScore = bundle.getInt("tweeter_score")
                if (tweeterData is User) {
                    this.tweeterData = tweeterData
                    twitterVM.initTweeter(tweeterData)
                    twitterVM.initTweeterScore("?")

                }

            }

        }
    }


    private fun initData() {

        configSpeedView()
        getTweeterFollowers(tweeterData)

        /** Observe tweeter followings data then search in CDP
        If following exist retrieve score else create new customer
         */

        twitterVM.followingData.observe(this, { response ->
            Timber.i("${response.users}")
            followingVM.checkFollowings(ArrayList(response.users?.take(20) ?: arrayListOf()))
        })

        /**
         * Observe following list  with their personal score.
         **/

        followingVM.followingsListWithScores.observe(this, { it ->
            it?.let { followings ->
                twitterVM.initTweeterScore(
                    getTotalScore(
                        followingVM.findFollowingsAvgScore(
                            followings
                        ), tweeterScore
                    )
                )
                openShareRank()

                updateFollowingsList(followings)

            }

        })

        twitterVM.tweeterScore.observe(this, {
            if (NumberExt.isNumber(it)) {
                fillView(it)
                followingVM.addFollowingsAvgScoreToCustomerdata(tweeterData!!, it)
            }
        })




        twitterVM.failure.observe(this,
            {
                it?.let {
                    checkFailureStatus(it)
                    getTweeterFollowers(tweeterData)

                }

            })


    }

    private fun updateFollowingsList(followings: List<User>) {
        followings.sortedBy { it.score_personal }.take(10).let {
            followingsAdapter.collection = it
            twitterVM.hideLoading()
        }

    }


    private fun configSpeedView() {
        binding.userContentLay.userChartLay.speedView.clearSections()
        binding.userContentLay.userChartLay.speedView.addSections(
            Section(
                .0f,
                .3f,
                Color.RED,
                binding.userContentLay.userChartLay.speedView.speedometerWidth,
                Style.BUTT
            ),
            Section(
                .3f,
                .5f,
                Color.YELLOW,
                binding.userContentLay.userChartLay.speedView.speedometerWidth,
                Style.BUTT
            ),
            Section(
                .5f,
                .99f,
                Color.GREEN,
                binding.userContentLay.userChartLay.speedView.speedometerWidth,
                Style.BUTT
            )
        )

    }

    private fun fillView(totalScore: String) {
        val displayScore = (totalScore.toInt() * 10)
        binding.userContentLay.userContentLay.audit.text = displayScore.toString() + "%"
        binding.userContentLay.userChartLay.speedView.speedTo(displayScore.toFloat(), 8)
        binding.userContentLay.userContentLay.chart.description.isEnabled = false


        var followings = tweeterData?.friends_count
        val active = ((followings?.toInt())!! * (totalScore.toInt() * 10)) / 100
        val inActive = ((followings?.toInt())!! * (100 - totalScore.toInt() * 10)) / 100

        binding.userContentLay.userContentLay.fakefollowes.text =
            inActive.toString() + "\nIn-active/bot/fake"
        binding.userContentLay.userContentLay.realfollowers.text =
            active.toString() + " Real"



        entries.add(PieEntry(inActive.toFloat(), "In-active/bot/fake"))
        colors.add(resources.getColor(R.color.colorRed))
        entries.add(PieEntry(active.toFloat(), "Real"))
        colors.add(resources.getColor(R.color.colorGreen))

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.colors = colors

        val pieData = PieData(pieDataSet)
        binding.userContentLay.userContentLay.chart.setDrawEntryLabels(false)
        binding.userContentLay.userContentLay.chart.data = pieData
        binding.userContentLay.userContentLay.chart.animateY(1400, Easing.EaseInOutQuad)


    }

    private fun getTotalScore(followingsScore: Int, tweeterScore: Int?): String {
        //max of followingScore is 5
        //max of totalScore is 10

        val followingsAvgScore = if (followingsScore > 5) {
            5
        } else {
            followingsScore
        }

        val totalScore = followingsAvgScore + (tweeterScore ?: 0)
        return if (totalScore > 10) {
            "10"
        } else {
            "$totalScore"
        }

    }

    fun getTweeterFollowers(userData: User?) {
        userData?.screen_name?.let {
            twitterVM.initScreenName(it)
            twitterVM.getFollowings()

        }

    }

    private fun openShareRank() {
        resume = false
        val handler = Handler()
        handler.postDelayed({
            twitterVM.openPopUp()

        }, 3500)

    }


    override fun openUserTwitter(item: User) {
        var intent: Intent

        try {
            // get the Twitter app if possible
            this.packageManager.getPackageInfo("com.twitter.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=${item.id}"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } catch (e: Exception) {
            // no Twitter app, revert to browser
            intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/${item.screen_name}"))
        }
        this.startActivity(intent)

    }

    override fun shareRank(item: User) {
        val shareTxt = "My twitter Rank is $tweeterScore/10 via @TwitterRankApp." +
                "\nhttps://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"


        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareTxt)
            startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: java.lang.Exception) {
            //e.toString();
        }
    }

    private fun checkFailureStatus(it: Failure) {
        when (it) {
            is Failure.FeatureFailure -> renderFailure(it.msgRes)
            is Failure.UNAUTHORIZED_Error -> {
                openAlertDialog(getString(R.string.auth_err))

            }
            is Failure.ServerError -> {
                openAlertDialog(getString(R.string.server_err))

            }
            is Failure.NetworkConnection -> {
                openAlertDialog(getString(R.string.no_internet))

            }
            else -> {
                openAlertDialog(getString(R.string.no_internet))
            }
        }

    }

    private fun renderFailure(message: String?) {
        Timber.e("renderFailure $message")
        message?.let {
            try {

                val jObjError = JSONObject(message)
                setErrorsToViews(jObjError)


            } catch (e: Exception) {
                Timber.e("${e.localizedMessage}")

            }
        }
    }

    private fun setErrorsToViews(jObjError: JSONObject) {
        if (jObjError.has("errors")) {
            val jsonArray = jObjError.getJSONArray("errors")
            jsonArray?.let {
                if (jsonArray.length() > 0 && jsonArray[0] is JSONObject) {
                    openAlertDialog((jsonArray[0] as JSONObject)["message"].toString())
                }
            }


        }

    }


    override fun onBackPressed() {
        if (twitterVM.openShare.value == true) {
            twitterVM.hidePopUp()
        } else {
            super.onBackPressed()

        }
    }


}