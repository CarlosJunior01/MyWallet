package com.carlosmagno.mywallet.di

import androidx.room.Room
import com.carlosmagno.mywallet.data.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "mywallet-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().accountDao() }
    single { get<AppDatabase>().transactionDao() }
}