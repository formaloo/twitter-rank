package co.idearun.twitter.di

import co.idearun.twitter.BuildConfig.*
import co.idearun.twitter.data.local.di.localModule
import co.idearun.twitter.data.remote.cdp.di.remoteCustomerModule
import co.idearun.twitter.data.remote.search.di.remoteSearchModule
import co.idearun.twitter.data.remote.twitter.di.createRemoteTwitterModule
import co.idearun.twitter.data.repository.cdp.di.customerRepositoryModule
import co.idearun.twitter.data.repository.search.di.searchRepositoryModule
import co.idearun.twitter.data.repository.twitter.di.twitterRepositoryModule
import co.idearun.twitter.feature.viewmodel.di.featureModule

val appComponent = listOf(
    createRemoteTwitterModule(),
    twitterRepositoryModule,
    localModule,
    featureModule,
    customerRepositoryModule,
    remoteCustomerModule(BASE_URL_CDP, X_API_KEY_CDP),
    searchRepositoryModule,
    remoteSearchModule(BASE_URL_CDP_SEARCH, X_API_KEY_CDP),
)