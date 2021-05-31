package co.idearun.twitter

import android.app.Application
import co.idearun.twitter.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        configureDi()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }



    }

    //     CONFIGURATION ---
    open fun configureDi() =
        startKoin {
            androidContext(this@App)
            // your modules
            modules(provideComponent())

        }

    //     PUBLIC API ---
    open fun provideComponent() = appComponent
}