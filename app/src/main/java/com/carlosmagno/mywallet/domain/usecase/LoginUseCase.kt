package com.carlosmagno.mywallet.domain.usecase

import com.carlosmagno.mywallet.domain.repository.UserRepository

class LoginUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}