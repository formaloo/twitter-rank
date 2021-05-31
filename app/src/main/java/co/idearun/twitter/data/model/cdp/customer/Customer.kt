package co.idearun.twitter.data.model.cdp.customer

import java.io.Serializable

data class Customer(
    val score: Int? = null,
    var twitter_id: String? = null,
    val full_name: String? = null,
    val code: String? = null,
    val updated_at: String? = null,
    val level: Int? = null,
    val last_name: String? = null,
    val created_at: String? = null,
    val phone_number: Any? = null,
    val first_name: String? = null,
    val email: String? = null,
    val slug: String? = null,
    val customer_data: HashMap<String,Any>? = null,
    val rendered_data: ArrayList<RenderedData>? = null,
    val tags: List<TagsItem?>? = null
) : Serializable
