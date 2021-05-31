package co.idearun.twitter.data.remote.twitter

import co.idearun.twitter.data.model.FollowingsResponse
import co.idearun.twitter.data.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitterService {

    companion object {
        const val VERSION1 = "1.1/"

        private const val FOLLOWING = "${VERSION1}friends/list.json"
        private const val USER = "${VERSION1}users/show.json"

    }

    @GET(FOLLOWING)
    fun getFollowings(@Query("screen_name")screen_name: String,@Query("count")count: Int): Call<FollowingsResponse>

    @GET(USER)
    fun getTweeter(@Query("screen_name") screen_name: String): Call<User>


}