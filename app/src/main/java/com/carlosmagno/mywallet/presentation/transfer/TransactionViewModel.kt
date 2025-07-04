package com.carlosmagno.mywallet.presentation.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosmagno.mywallet.domain.repository.AccountRepository
import com.carlosmagno.mywallet.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _transferState = MutableStateFlow<TransferState>(TransferState.Idle)
    val transferState: StateFlow<TransferState> get() = _transferState

    fun transfer(fromUserId: Int, toEmail: String, amount: Double) {
        viewModelScope.launch {
            _transferState.value = TransferState.Loading

            val toUser = userRepository.getUserByEmail(toEmail)

            if (toUser == null) {
                _transferState.value = TransferState.Error("Usuário destinatário não encontrado")
                return@launch
            }

            val success = accountRepository.transfer(fromUserId, toUser.id, amount)

            _transferState.value = if (success) {
                TransferState.Success
            } else {
                TransferState.Error("Falha na transferência. Verifique saldo e dados.")
            }
        }
    }

    sealed class TransferState {
        object Idle : TransferState()
        object Loading : TransferState()
        object Success : TransferState()
        data class Error(val message: String) : TransferState()
    }
}
