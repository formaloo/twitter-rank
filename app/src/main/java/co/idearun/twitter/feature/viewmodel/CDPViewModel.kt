package co.idearun.twitter.feature.viewmodel

import android.util.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.idearun.twitter.common.TimeAgo2
import co.idearun.twitter.common.TweeterActions
import co.idearun.twitter.data.model.User
import co.idearun.twitter.data.model.cdp.activity.ActivityResponse
import co.idearun.twitter.data.model.cdp.customer.Customer
import co.idearun.twitter.data.model.cdp.customer.CustomerSearchRes
import co.idearun.twitter.data.repository.cdp.CDPRepository
import co.idearun.twitter.data.repository.search.SearchRepository
import co.idearun.twitter.feature.base.BaseViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber

class CDPViewModel(
    private val repository: CDPRepository,
    private val searchRepo: SearchRepository
) : BaseViewModel() {

    private val _customer = MutableLiveData<Customer>().apply { value = null }
    val customer: LiveData<Customer> = _customer

    val _followingsListWithScores = MutableLiveData<List<User>>().apply { value = null }
    val followingsListWithScores: LiveData<List<User>> = _followingsListWithScores

    fun checkFollowings(users: ArrayList<User>) = launch {
        _followingsListWithScores.value = searchUserFollowings(users)

    }


    /**
     *This method get list of [User]s (here as followings list).
     * Search each user in CDP database.
     * If user exists fetch his/her score and fill [User.score_personal]
     * else create new customer[createCustomerList] with user data then fetch score.
     * Finally return a list of user with their scores.
     * */

    suspend fun searchUserFollowings(users: ArrayList<User>): ArrayList<User> {
        val allUsers = users
        val jobs = arrayListOf<CustomerSearchRes?>()
        val existUsers = arrayListOf<User>()
        val allUsersWithScore = arrayListOf<User>()
        withContext(Dispatchers.IO) {
            users.map { user ->
                val screenName = user.screen_name
                val job = async {
                    searchRepo.searchUser("$screenName@twitter.com")
                }.await()

                jobs.add(job)

            }

            jobs.forEachIndexed { index, response ->
                val customers = response?.data?.customers
                users.forEach { user ->
                    customers?.find { customer ->
                        customer?.email == "${user.screen_name}@twitter.com"
                    }?.let { customer ->
                        existUsers.add(user)
                        user.score_personal = customer.score
                        allUsersWithScore.add(user)
                    }
                }

            }
            allUsers.removeAll(existUsers)
            allUsersWithScore.addAll(createCustomerList(allUsers))

        }

        return allUsersWithScore
    }


    /**
     * Save a list of user in CDP database
     * Calculate user score in [createRequestBody] and [addVerifiedActionRequest] method.
     * Return a list of saved user in CDP with total score
     */
    suspend fun createCustomerList(notExistUsers: ArrayList<User>): ArrayList<User> {
        val usersWithScore = arrayListOf<User>()

        withContext(Dispatchers.IO) {
            notExistUsers.map { user ->
                async {
                    if (chechVerified(user)) {
                        createActivity(addVerifiedActionRequest(user))
                    } else {
                        null
                    }
                }

                async {
                    createActivity(createRequestBody(user))
                }


            }.awaitAll().map {
                it?.data?.activity?.customer?.let { customer ->
                    notExistUsers.find {
                        customer.email == "${it.screen_name}@twitter.com"
                    }?.let { user ->
                        user.score_personal = customer.score
                        usersWithScore.add(user)
                    }
                }

            }
        }

        return usersWithScore
    }


    /**
     *This method get a [User].
     * Search the user in CDP database.
     * If user exists fetch his/her score and fill [User.score_personal]
     * else create new customer[createCustomerList] with user data then fetch score.
     * */
    fun searchUser(user: User) = launch {
        var customerData: Customer? = null
        withContext(Dispatchers.IO) {
            async {
                searchRepo.searchUser("${user.screen_name}@twitter.com")
            }.await().let { response ->
                if (response == null) {
                    createCustomer(user)
                } else {
                    val customers = response.data?.customers
                    customers?.find { customer ->
                        customer?.email == "${user.screen_name}@twitter.com"
                    }.let { customer ->
                        customerData = customer ?: createCustomer(user)
                    }
                }
            }

        }

        customerData?.let {
            _customer.value = it
        }
    }


    /**
     * Save a user as a customer in CDP .
     * Calculate user score in [createRequestBody] and [addVerifiedActionRequest] method.
     */

    suspend fun createCustomer(user: User): Customer? {
        var customer: Customer? = null
        withContext(Dispatchers.IO) {
            async {
                if (chechVerified(user)) {
                    val createActivity = createActivity(addVerifiedActionRequest(user))
                    customer = createActivity?.data?.activity?.customer
                } else {
                    null
                }
            }.await().let {
                async {
                    createActivity(createRequestBody(user))
                }.await()?.let { response ->
                    customer = response.data?.activity?.customer

                }
            }

        }

        return customer
    }

    // create activity for customer
    private suspend fun createActivity(req: RequestBody): ActivityResponse? {
        return repository.addActivity(req)

    }

    /** Create request body (specify tag and Equivalent action) for [addActivity] method
     *  calculate score and get tag and equivalent action [fetchCustomerActions]
     */
    fun createRequestBody(user: User): RequestBody {

        val req = ArrayMap<String, Any>()

        val typeScore = fetchCustomerActions(user)
        val tag = typeScore.tagSlug
        val action = typeScore.actionSlug


        //Tags help us to now witch actions is done by a customer.
        //[TweeterActions.kt] display the list of tweeter actions.
        //we assign each action, a tag. In this way we can observe actions of a customer

        val tagArray = JSONArray()
        tagArray.put(0, JSONObject().put("title", tag))



        req["tags"] = tagArray
        req["customer"] = createCustomerBody(user)
        req["action"] = action


        return RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(req).toString()
        )


    }


    //create request body for verified user
    fun addVerifiedActionRequest(user: User): RequestBody {
        val req = ArrayMap<String, Any>()

        val tag = TweeterActions.VERIFIED.tagSlug
        val action = TweeterActions.VERIFIED.actionSlug
        val tagArray = JSONArray()
        tagArray.put(0, JSONObject().put("title", tag))

        req["tags"] = tagArray
        req["customer"] = createCustomerBody(user)
        req["action"] = action

        return RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(req).toString()
        )

    }

    // calculate following average score
    fun findFollowingsAvgScore(allCustomers: List<User>): Int {
        var totalScore = 0
        allCustomers.forEach {
            totalScore += it.score_personal ?: 0
        }

        return if (totalScore < 1) {
            0
        } else {
            totalScore / allCustomers.size
        }
    }

    private fun createCustomerBody(user: User): ArrayMap<String, Any> {

        val customer = ArrayMap<String, Any>()
        val customer_data = ArrayMap<String, Any>()

        customer["full_name"] = user.name
        customer["email"] = "${user.screen_name}@twitter.com"

        customer_data["screen_name"] = user.screen_name
        customer_data["description"] = user.description
        customer_data["location"] = user.location
        customer_data["profile_image_url_https"] = user.profile_image_url_https
        customer_data["created_at"] = user.created_at
        customer_data["statuses_count"] = user.statuses_count.toString()
        customer_data["friends_count"] = user.friends_count.toString()
        customer_data["listed_count"] = user.listed_count.toString()
        customer_data["favourites_count"] = user.favourites_count.toString()
        customer_data["verified"] = user.verified.toString()

        customer["customer_data"] = customer_data

        return customer
    }

    fun chechVerified(userData: User): Boolean {
        return userData.verified == true
    }

    // calculate user score and give equivalent tag and action
    fun fetchCustomerActions(userData: User): TweeterActions {
        val followers: Long = userData.followers_count ?: 0
        val following: Long = userData.friends_count ?: 0
        val tweets: Long = userData.statuses_count ?: 0
        val hasPhoto = userData.profile_image_url_https?.isNotEmpty() ?: false

        val ageOfAccount = TimeAgo2().convertAgeToYear(userData.created_at)

        val followersDivideFollowing = if (following > 0) {
            followers / following
        } else {
            0
        }

        return when {
            (hasPhoto && ageOfAccount >= 10 && tweets <= 10000 && followersDivideFollowing >= 2) -> {
                //If user has this action_type , we add new activity to him/her with action_type_slug and set equivalent tag
                TweeterActions.FIRST_GRADE
            }
            (hasPhoto && ageOfAccount >= 5 && tweets <= 15000 && followersDivideFollowing >= 1.3) -> {
                TweeterActions.SECOND_GRADE
            }
            (hasPhoto && ageOfAccount >= 3 && tweets <= 20000 && followersDivideFollowing >= 1) -> {
                TweeterActions.THIRD_GRADE
            }
            (ageOfAccount <= 2 && followersDivideFollowing >= 1) -> {
                TweeterActions.FOURTH_GRADE
            }
            (ageOfAccount >= 1 && followersDivideFollowing <= 1) -> {
                TweeterActions.FIFTH_GRADE
            }
            else -> {
                TweeterActions.NONE
            }
        }

    }


    fun createFollowingsAvgScoreRequestBody(user: User, score: String): RequestBody {

        val req = ArrayMap<String, Any>()
        val customer = ArrayMap<String, Any>()
        val customer_data = ArrayMap<String, Any>()

        customer["full_name"] = user.name
        customer["email"] = "${user.screen_name}@twitter.com"
        customer_data["followings_avg_score"] = score

        customer["customer_data"] = customer_data

        req["customer"] = customer
        req["action"] = TweeterActions.NONE.actionSlug


        return RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(req).toString()
        )


    }

    fun addFollowingsAvgScoreToCustomerdata(user: User, score: String) = launch {
        val result = async(Dispatchers.IO) {
            repository.createActivity(
                createFollowingsAvgScoreRequestBody(
                    user,
                    score
                )
            )
        }.await()
        result.either(::handleFailure, ::handleTotalScore)
    }


    private fun handleTotalScore(res: ActivityResponse) {
        res?.let {
        }

    }


}