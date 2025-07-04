package com.carlosmagno.mywallet.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosmagno.mywallet.data.local.TransactionEntity
import com.carlosmagno.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionHistoryViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactions: StateFlow<List<TransactionEntity>> = _transactions

    fun loadTransactions(userId: Int) {
        viewModelScope.launch {
            val allTransactions = transactionRepository.getAll()
            val userTransactions = allTransactions.filter {
                it.fromUserId == userId || it.toUserId == userId
            }
            _transactions.value = userTransactions
        }
    }
}