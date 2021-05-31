package co.idearun.twitter.data.model.cdp.activity

import co.idearun.twitter.data.model.cdp.customer.Customer
import java.io.Serializable

data class ActivityResponse(
	val data: Data? = null,
	val errors: Errors? = null,
	val status: Int? = null
): Serializable {
	companion object{
		fun empty() = ActivityResponse(null,null,null)
	}
}

data class Data(
	val activity: Activity? = null
)

data class Activity(
	val operationType: String? = null,
	val updatedAt: String? = null,
	val activityDate: String? = null,
	val action: String? = null,
	val description: String? = null,
	val createdAt: String? = null,
	val currency: Any? = null,
	val monetaryValue: String? = null,
	val activityData: ActivityData? = null,
	val relations: List<Any?>? = null,
	val slug: String? = null,
	val customer: Customer? = null
)

data class Errors(
	val formErrors: FormErrors? = null,
	val generalErrors: List<Any?>? = null
)

data class ActivityData(
	val ticketCode: String? = null
)

data class FormErrors(
	val any: Any? = null
)

