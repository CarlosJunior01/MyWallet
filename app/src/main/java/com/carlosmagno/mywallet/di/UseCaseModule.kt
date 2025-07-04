package com.carlosmagno.mywallet.di

import com.carlosmagno.mywallet.domain.usecase.LoginUseCase
import com.carlosmagno.mywallet.domain.usecase.TransferUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { TransferUseCase(get()) }
}