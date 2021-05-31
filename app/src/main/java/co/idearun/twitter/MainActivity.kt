package co.idearun.twitter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import co.idearun.twitter.common.exception.Failure
import co.idearun.twitter.data.model.User
import co.idearun.twitter.databinding.ActivityMainBinding
import co.idearun.twitter.feature.AboutActivity
import co.idearun.twitter.feature.BaseActivity
import co.idearun.twitter.feature.MainListener
import co.idearun.twitter.feature.TweeterActivity
import co.idearun.twitter.feature.viewmodel.*
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : BaseActivity(), MainListener {

    private var userData: User? = null
    val twitterVM: TwitterViewModel by viewModel()
    val cdpVM: CDPViewModel by viewModel()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewmodel = twitterVM
        binding.mainContentLay.viewmodel = twitterVM
        binding.mainContentLay.listener = this
        binding.lifecycleOwner = this

        // CDP Version

        /** retrieve user profile with given username from twitter -> twitterVM.getUser() [observeProfile]
         * search user profile in Formaloo CDP, if user already saved in the CDP retrieve user data and score
         * else create a customer with user data into CDP -> cdpVM.searchUser(user) [observeCustomer]
         * then redirect to [TweeterActivity] with openUserPage
         *
         *
         *userTotalScore = personalScore + followingsScore
         * As [TwitterService.getUser] does not return followings list data we can only calculate
         * personalScore with [fetchCustomerActions] method in [CDPViewModel] at first step
         *
         * so we have to calculate user_followings_score after retrieve followingList from [TwitterService.getFollowings]
         *
         * */


        initData()
    }

    private fun initData() {

        twitterVM.profile.observe(this, {
            Timber.i("$it")
            it?.let { user ->
                userData = user
                cdpVM.searchUser(user)
            }
        })

        cdpVM.customer.observe(this, { customer ->
            customer?.let {
                val score = customer.score
                openUserPage(userData, score)
            }
        })

        twitterVM.failure.observe(this, {
            Timber.i("$it")
            it?.let {
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
                }
            }
            twitterVM.hideLoading()

        })

    }


    private fun openUserPage(user: User?, userScore: Int?) {
        twitterVM.hideLoading()
        val intent = Intent(this, TweeterActivity::class.java)
        intent.putExtra("tweeter", user)
        intent.putExtra("tweeter_score", userScore)
        startActivity(intent)
    }

    override fun openFormaloo() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(getString(R.string.formaloo_web))
        startActivity(intent)
    }

    override fun openInfo() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
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
                    twitterVM.errorFind(jsonArray[0] as JSONObject)

                }
            }


        }

    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

        try {
            return super.dispatchTouchEvent(ev)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    override fun onResume() {
        super.onResume()
    }

}