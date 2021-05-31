package co.idearun.twitter.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("id")
    var id: String?=null,
    @SerializedName("id_str")
    var id_str: String?=null,
    @SerializedName("name")
    var name: String?=null,
    @SerializedName("screen_name")
    var screen_name: String?=null,
    @SerializedName("location")
    var location: String?=null,
    @SerializedName("description")
    var description: String?=null,
    @SerializedName("profile_image_url_https")
    var profile_image_url_https: String?=null,
    @SerializedName("created_at")
    var created_at: String?=null,
    @SerializedName("followers_count")
    var followers_count: Long?=null,
    @SerializedName("statuses_count")
    var statuses_count: Long?=null,
    @SerializedName("friends_count")
    var friends_count: Long?=null,
    @SerializedName("listed_count")
    var listed_count: Long?=null,
    @SerializedName("favourites_count")
    var favourites_count: Long?=null,
    @SerializedName("score_personal")
    var score_personal: Int?=null,
    @SerializedName("verified")
    var verified: Boolean?=null,
) : Serializable {
    companion object {
        fun empty() = User(null, null, null, null,null, null, null, null, null, null, null, null,null, null, null)

    }

    fun toUser() = User(
        id,
        id_str,
        name,
        screen_name,
        location,
        description,
        profile_image_url_https,
        created_at,
        followers_count,
        statuses_count,
        friends_count,
        listed_count,
        favourites_count,
        score_personal,
        verified
    )
}