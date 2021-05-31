package co.idearun.twitter.data.repository.cdp

import android.annotation.SuppressLint
import co.idearun.twitter.common.exception.Failure
import co.idearun.twitter.common.exception.ViewFailure
import co.idearun.twitter.common.functional.Either
import co.idearun.twitter.data.model.cdp.activity.ActivityResponse
import co.idearun.twitter.data.remote.cdp.CDPDataSource
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import timber.log.Timber
import java.net.UnknownHostException

interface CDPRepository {
    suspend fun addActivity(body: RequestBody): ActivityResponse?
    suspend fun createActivity(body: RequestBody?): Either<Failure, ActivityResponse>

}

class CustomerRepositoryImpl(
    private val source: CDPDataSource
) : CDPRepository {


    /**
     * Formaloo CDP calculate a customer score based on his/her activities.
     * To assign a score to an activity you need to create an action. [CreateActivity](https://docs.formaloo.com/v1.0/?python#create-activity)
     *
     * You can send customer data as createActivity parameter.
     * If customer already exists cdp just update the data else create new customer
     *
     */
    override suspend fun addActivity(req: RequestBody): ActivityResponse? {
        val call = source.createActivity(req).execute()
        return call.body()

    }
    override suspend fun createActivity(body: RequestBody?): Either<Failure, ActivityResponse> {
        Timber.e("createActivity")
        val call = source.createActivity(body)
        return try {
            request(call, { it }, ActivityResponse.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }

    }

    @SuppressLint("LogNotTimber")
    private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
        return try {
            val response = call.execute()
            var jObjError: JSONObject? = null
            Timber.e("raw ${response.raw()}")
            Timber.e("body ${response.body()}")

            try {
                jObjError = JSONObject(response.errorBody()?.string())
                Timber.e("Repo responseErrorBody jObjError-> $jObjError")
            } catch (e: Exception) {

            }

            when (response.code()) {
                200 -> Either.Right(transform((response.body() ?: default)))
                201 -> Either.Right(transform((response.body() ?: default)))
                400 -> Either.Left(ViewFailure.responseError("$jObjError"))
                401 -> Either.Left(Failure.UNAUTHORIZED_Error)
                500 -> Either.Left(Failure.ServerError)
                else -> Either.Left(ViewFailure.responseError("$jObjError"))
            }

        } catch (exception: Throwable) {
            Timber.e("exception $exception")
            if (exception is UnknownHostException) {
                Either.Left(Failure.NetworkConnection)

            } else {
                Either.Left(ViewFailure.responseError("exception++>  $exception"))

            }
        }

    }
}