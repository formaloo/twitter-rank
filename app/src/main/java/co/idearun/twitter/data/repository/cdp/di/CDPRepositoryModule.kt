package co.idearun.twitter.data.repository.cdp.di

import co.idearun.twitter.data.repository.cdp.CDPRepository
import co.idearun.twitter.data.repository.cdp.CustomerRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val customerRepositoryModule = module {
    factory(named("CustomerRepositoryImpl")) {
        CustomerRepositoryImpl(get(named("CustomerDataSource"))) as CDPRepository
    }

}