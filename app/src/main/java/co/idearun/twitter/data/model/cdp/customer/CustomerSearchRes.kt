package co.idearun.twitter.data.model.cdp.customer

import java.io.Serializable

data class CustomerSearchRes(
	val data: Data? = null,
	val status: Int? = null,
	var twitter_id: String? = null
) : Serializable {
    companion object {
        fun empty() = CustomerSearchRes(null, 0)
    }

    fun toActivityRes() = CustomerSearchRes(null, 0)
}


data class TagsItem(
	val updatedAt: String? = null,
	val description: Any? = null,
	val createdAt: String? = null,
	val title: String? = null,
	val colorCode: Any? = null,
	val slug: String? = null
)

data class Errors(
	val formErrors: FormErrors? = null,
	val generalErrors: List<Any?>? = null
)

data class Data(
	val next: Any? = null,
	val previous: Any? = null,
	val count: Int? = null,
	val customers: List<Customer?>? = null,
	val pageCount: Int? = null,
	val currentPage: Int? = null,
	val pageSize: Int? = null
)

data class FormErrors(
	val any: Any? = null
)

