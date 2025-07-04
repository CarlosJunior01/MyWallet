package com.carlosmagno.mywallet.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosmagno.mywallet.data.local.AccountEntity
import com.carlosmagno.mywallet.domain.model.User
import com.carlosmagno.mywallet.domain.repository.AccountRepository
import com.carlosmagno.mywallet.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> get() = _registerState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            val newUser = User(
                id = 0,
                name = name,
                email = email
            )

            val success = userRepository.register(newUser, password)

            if (success) {
                val registeredUser = userRepository.login(email, password)
                registeredUser?.let { user ->
                    accountRepository.createAccount(
                        AccountEntity(
                            userId = user.id,
                            balance = 1000.0
                        )
                    )
                }
                _registerState.value = RegisterState.Success
            } else {
                _registerState.value = RegisterState.Error("Erro ao registrar usuário")
            }
        }
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}