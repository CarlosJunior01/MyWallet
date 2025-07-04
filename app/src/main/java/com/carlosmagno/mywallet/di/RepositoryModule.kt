package com.carlosmagno.mywallet.di

import com.carlosmagno.mywallet.data.repositoryImpl.AccountRepositoryImpl
import com.carlosmagno.mywallet.data.repositoryImpl.TransactionRepositoryImpl
import com.carlosmagno.mywallet.data.repositoryImpl.UserRepositoryImpl
import com.carlosmagno.mywallet.domain.repository.AccountRepository
import com.carlosmagno.mywallet.domain.repository.TransactionRepository
import com.carlosmagno.mywallet.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AccountRepository> { AccountRepositoryImpl(get(), get()) }
    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
}
