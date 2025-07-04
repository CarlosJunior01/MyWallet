package com.carlosmagno.mywallet

import android.app.Application
import com.carlosmagno.mywallet.di.databaseModule
import com.carlosmagno.mywallet.di.repositoryModule
import com.carlosmagno.mywallet.di.useCaseModule
import com.carlosmagno.mywallet.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}