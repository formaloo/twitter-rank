package co.idearun.twitter.data.repository.search

import android.annotation.SuppressLint
import co.idearun.twitter.common.exception.Failure
import co.idearun.twitter.common.exception.ViewFailure
import co.idearun.twitter.common.functional.Either
import co.idearun.twitter.data.model.cdp.customer.CustomerSearchRes
import co.idearun.twitter.data.remote.search.SearchDataSource
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.net.UnknownHostException

interface SearchRepository {
    suspend fun searchCustomer(twitterId: String): Either<Failure, CustomerSearchRes>
    suspend fun searchUser(twitterId: String): CustomerSearchRes?
}

class SearchRepositoryImpl(
    private val source: SearchDataSource,
) : SearchRepository {


    override suspend fun searchCustomer(twitterId: String): Either<Failure, CustomerSearchRes> {
        val call = source.searchCustomer(twitterId)
        return try {
            request(call, { it }, CustomerSearchRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }


    override suspend fun searchUser(twitterId: String): CustomerSearchRes? {
        val call = source.searchCustomer(twitterId).execute()
        return call.body()

    }

    @SuppressLint("LogNotTimber")
    private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
        return try {
            val response = call.execute()
            var jObjError: JSONObject? = null
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