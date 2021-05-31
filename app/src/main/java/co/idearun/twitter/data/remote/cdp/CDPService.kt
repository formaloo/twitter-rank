package co.idearun.twitter.data.remote.cdp

import co.idearun.twitter.data.model.cdp.activity.ActivityResponse
import co.idearun.twitter.data.model.cdp.customer.CustomerDetailRes
import co.idearun.twitter.data.model.cdp.customer.CustomerList
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CDPService {

    companion object {
        const val VERSION10 = "v1.0/"
        const val VERSION20 = "v2/"

        //Create a new customer on the your business.
        private const val CUSTOMERS_CREATE = "${VERSION10}customers/"

        //Get/PATCH a customer's data.
        private const val CUSTOMER_Detail = "${VERSION10}customers/{code}/"

        //Get/PATCH a activity's data.
        private const val CREATE_ACTIVITY = "${VERSION10}activities/"


    }

    @Multipart
    @POST(CUSTOMERS_CREATE)
    fun createCustomers(
        @PartMap req: HashMap<String, RequestBody>?
    ): Call<CustomerDetailRes>

    @POST(CUSTOMERS_CREATE)
    fun createCustomers(
        @Body req: RequestBody?
    ): Call<CustomerDetailRes>

    @POST(CREATE_ACTIVITY)
    fun createActivity(
        @Body req: RequestBody?, @Query("full_data") full_data: Int
    ): Call<ActivityResponse>

    @PATCH(CUSTOMER_Detail)
    fun editCustomer(
        @Path("code") code: String,
        @Body body: RequestBody?
    ): Call<CustomerDetailRes>

    @GET(CUSTOMER_Detail)
    fun getCustomer(@Path("code") code: String): Call<CustomerDetailRes>

    @GET(CUSTOMERS_CREATE)
    fun getCustomers(): Call<CustomerList>

}