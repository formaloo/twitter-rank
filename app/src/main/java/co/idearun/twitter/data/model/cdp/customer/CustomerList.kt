package co.idearun.twitter.data.model.cdp.customer

import java.io.Serializable

data class CustomerList(
	val next: String? = null,
	val previous: String? = null,
	val count: Int? = null,
	val customers: List<Customer?>? = null,
	val pageCount: Int? = null,
	val currentPage: Int? = null,
	val pageSize: Int? = null
): Serializable {

	companion object{
		fun empty() = CustomerList("","",0,null,0,0,0)
	}
}




