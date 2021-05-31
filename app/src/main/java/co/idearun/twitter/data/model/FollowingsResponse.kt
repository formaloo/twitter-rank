package co.idearun.twitter.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FollowingsResponse(
    @SerializedName("users")
    val users: ArrayList<User>?,
    @SerializedName("next_cursor_str")
    val next_cursor_str: String?,
    @SerializedName("previous_cursor_str")
    val previous_cursor_str: String?,
    @SerializedName("total_count")
    val total_count: Int?
): Serializable {
    companion object {
        fun empty() = FollowingsResponse(null,null, null,null)


    }

    fun toFollowersResponse() = FollowingsResponse(users, next_cursor_str,previous_cursor_str,total_count)

}