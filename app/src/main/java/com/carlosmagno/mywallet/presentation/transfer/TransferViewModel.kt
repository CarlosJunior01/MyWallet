package com.carlosmagno.mywallet.presentation.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosmagno.mywallet.data.local.TransactionEntity
import com.carlosmagno.mywallet.domain.repository.AccountRepository
import com.carlosmagno.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransferViewModel(
    private val accountRepo: AccountRepository,
    private val transactionRepo: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TransferState>(TransferState.Idle)
    val state: StateFlow<TransferState> = _state

    fun transfer(fromId: Int, toId: Int, amount: Double) {
        viewModelScope.launch {
            val from = accountRepo.getAccountByUserId(fromId)
            val to = accountRepo.getAccountByUserId(toId)

            if (from == null || to == null || from.balance < amount) {
                _state.value = TransferState.Error("Transferência inválida")
                return@launch
            }

            accountRepo.updateAccount(from.copy(balance = from.balance - amount))
            accountRepo.updateAccount(to.copy(balance = to.balance + amount))

            val transaction = TransactionEntity(
                fromUserId = fromId,
                toUserId = toId,
                amount = amount,
                timestamp = System.currentTimeMillis()
            )
            transactionRepo.insert(transaction)

            _state.value = TransferState.Success
        }
    }
}
