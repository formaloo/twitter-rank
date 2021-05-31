package co.idearun.twitter.data.remote.twitter.di

import co.idearun.twitter.BuildConfig.*
import co.idearun.twitter.data.remote.twitter.TwitterDataSource
import co.idearun.twitter.data.remote.twitter.TwitterService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createRemoteTwitterModule() = module {

    factory(named("twitterInterceptor")) {
        Interceptor { chain ->
            val original = chain.request()

            val request: Request
            request = original.newBuilder()
                .header("Authorization", "Bearer $BEARER_TOKEN")
                .method(original.method, original.body)
                .build()


            chain.proceed(request)
        }
    }
//

    single(named("client")) {
        OkHttpClient.Builder()
            .addInterceptor(get(named("twitterInterceptor"))as Interceptor)
//            .connectTimeout(3, TimeUnit.MINUTES)
//            .readTimeout(3, TimeUnit.MINUTES)
            .build()
    }

    factory(named("retrofit")) {


        Retrofit.Builder()
            .client(get(named("client")))
            .baseUrl(BASE_URL_TWITTER)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory(named("service")) { get<Retrofit>(named("retrofit")).create(TwitterService::class.java) }

    factory(named("twitterDatasource")) { TwitterDataSource(get(named("service"))) }

}



