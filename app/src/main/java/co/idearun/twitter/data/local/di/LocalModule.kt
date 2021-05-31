package co.idearun.twitter.data.local.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val DATABASE = "DATABASE"
private const val Preferences = "Preferences"

val localModule = module {
//    single() { AppDatabase.buildDatabase(androidContext()) }
    single {
        provideSharePreferences(androidApplication())
    }
//    factory { (get() as AppDatabase).userDao() }

}

private fun provideSharePreferences(app: Application): SharedPreferences =
    app.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)