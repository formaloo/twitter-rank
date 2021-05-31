package co.idearun.twitter.data.remote.search.di

import co.idearun.twitter.data.remote.search.SearchDataSource
import co.idearun.twitter.data.remote.search.SearchService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun remoteSearchModule(baseUrl: String, appToken: String) = module {

    factory(named("searchInterceptor")) {
        Interceptor { chain ->
            val original = chain.request()
            val request: Request
            request = original.newBuilder()
                .header("x-api-key", appToken)
                .method(original.method, original.body)
                .build()


            chain.proceed(request)
        }
    }

    single(named("searchClient")) {
        OkHttpClient.Builder()
            .addInterceptor(get(named("searchInterceptor")) as Interceptor)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .build()

    }

    factory(named("searchRetrofit")) {
        Retrofit.Builder()
            .client(get(named("searchClient")))
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory(named("SearchService")) {
        get<Retrofit>(named("searchRetrofit")).create(SearchService::class.java)
    }

    factory(named("SearchDataSource")) {
        SearchDataSource(get(named("SearchService")))
    }

}


