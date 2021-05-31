package co.idearun.twitter.data.repository.twitter

import co.idearun.twitter.common.exception.Failure
import co.idearun.twitter.common.exception.ViewFailure
import co.idearun.twitter.common.functional.Either
import co.idearun.twitter.data.model.FollowingsResponse
import co.idearun.twitter.data.model.User
import co.idearun.twitter.data.remote.twitter.TwitterDataSource
import org.json.JSONObject
import retrofit2.Call
import timber.log.Timber

interface TwitterRepository {
    suspend fun getFollowings(screen_name: String): Either<Failure, FollowingsResponse>?
    suspend fun getTweeter(screen_name: String): Either<Failure, User>?
}

class TwitterRepositoryImpl(
    private val datasource: TwitterDataSource
) : TwitterRepository {


    override suspend fun getFollowings(screen_name: String): Either<Failure, FollowingsResponse> {
        val call = datasource.getFollowings(screen_name)
        return try {
            request(call, { it.toFollowersResponse() }, FollowingsResponse.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }

    }

    override suspend fun getTweeter(screen_name: String): Either<Failure, User> {
        val call = datasource.getTweeter(screen_name)
        return try {
            request(call, { it.toUser() }, User.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }

    }


    private fun <T, R> request(
        call: Call<T>,
        transform: (T) -> R,
        default: T
    ): Either<Failure, R> {
        return try {
            val response = call.execute()
            var jObjError: JSONObject? = null
            try {
                jObjError = JSONObject(response.errorBody()?.string())

            } catch (e: Exception) {

            }

            when (response.isSuccessful) {
                true -> Either.Right(transform((response.body() ?: default)))
                false -> Either.Left(co.idearun.twitter.common.exception.ViewFailure.responseError("$jObjError"))

            }

            when (response.code()) {
                200 -> {
                    Either.Right(transform((response.body() ?: default)))
                }
                201 -> {
                    Either.Right(transform((response.body() ?: default)))
                }
                500 -> {
                    Either.Left(Failure.ServerError)
                }
                403 -> {
                    Either.Left(ViewFailure.responseError("$jObjError"))
                }
                429 -> {
                    Either.Left(ViewFailure.responseError("$jObjError"))
                }
                else -> {
                    Either.Left(ViewFailure.responseError("$jObjError"))
                }

            }

        } catch (exception: Throwable) {
            Timber.e("exception $exception")
            Either.Left(Failure.NetworkConnection)

        }

    }

}