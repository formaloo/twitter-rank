package co.idearun.twitter.data.repository.twitter.di

import co.idearun.twitter.data.repository.AppDispatchers
import co.idearun.twitter.data.repository.twitter.TwitterRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val twitterRepositoryModule = module {
    factory { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
    factory(named("twitterRepositoryImpl")) {
        TwitterRepositoryImpl(get(named("twitterDatasource")))
    }
}