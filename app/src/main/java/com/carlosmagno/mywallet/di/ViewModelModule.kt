package com.carlosmagno.mywallet.di

import com.carlosmagno.mywallet.presentation.auth.LoginViewModel
import com.carlosmagno.mywallet.presentation.auth.RegisterViewModel
import com.carlosmagno.mywallet.presentation.dashboard.DashboardViewModel
import com.carlosmagno.mywallet.presentation.history.TransactionHistoryViewModel
import com.carlosmagno.mywallet.presentation.transfer.TransactionViewModel
import com.carlosmagno.mywallet.presentation.transfer.TransferViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { TransferViewModel(get(), get()) }
    viewModel { RegisterViewModel(userRepository = get(), accountRepository = get()) }
    viewModel { DashboardViewModel(get(), get()) }
    viewModel { TransactionHistoryViewModel(get()) }
    viewModel { TransactionViewModel(get(), get()) }
}
