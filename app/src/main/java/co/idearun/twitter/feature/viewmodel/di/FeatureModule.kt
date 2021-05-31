package co.idearun.twitter.feature.viewmodel.di

import co.idearun.twitter.feature.viewmodel.CDPViewModel
import co.idearun.twitter.feature.viewmodel.TwitterViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val featureModule = module {
    viewModel() { TwitterViewModel(get(named("twitterRepositoryImpl")), get()) }
    viewModel() {
        CDPViewModel(
            get(named("CustomerRepositoryImpl")),
            get(named("SearchRepositoryImpl"))
        )
    }

}