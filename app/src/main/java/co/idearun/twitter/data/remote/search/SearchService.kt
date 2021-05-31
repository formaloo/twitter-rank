package co.idearun.twitter.data.remote.search

import co.idearun.twitter.data.model.cdp.activity.ActivityRes
import co.idearun.twitter.data.model.cdp.customer.CustomerDetailRes
import co.idearun.twitter.data.model.cdp.customer.CustomerList
import co.idearun.twitter.data.model.cdp.customer.CustomerSearchRes
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface SearchService {

    companion object {
        const val VERSION1 = "production/"

        //Create a new customer on the your business.
        private const val SEARCH_CUSTOMER = "${VERSION1}customers"

    }

    @GET(SEARCH_CUSTOMER)
    fun searchCustomer(@Query("email") email:String,@Query("full_data") full_data:Int): Call<CustomerSearchRes>

}