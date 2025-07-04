package com.carlosmagno.mywallet.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosmagno.mywallet.domain.repository.AccountRepository
import com.carlosmagno.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> get() = _uiState

    fun loadDashboardData(userId: Int) {
        viewModelScope.launch {
            val account = accountRepository.getAccountByUserId(userId)
            val balance = account?.balance ?: 0.0

            val userTransactions = transactionRepository.getAllByUser(userId)

            _uiState.value = DashboardState(balance, userTransactions.size)
        }
    }

    data class DashboardState(
        val balance: Double = 0.0,
        val totalTransactions: Int = 0
    )
}

