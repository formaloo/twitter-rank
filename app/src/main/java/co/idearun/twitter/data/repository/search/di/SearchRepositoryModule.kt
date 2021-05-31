package co.idearun.twitter.data.repository.search.di

import co.idearun.twitter.data.repository.search.SearchRepository
import co.idearun.twitter.data.repository.search.SearchRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchRepositoryModule = module {
    factory(named("SearchRepositoryImpl")) {
        SearchRepositoryImpl(
            get(named("SearchDataSource")),
        ) as SearchRepository
    }

}