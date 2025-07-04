package com.carlosmagno.mywallet.domain.usecase

import com.carlosmagno.mywallet.domain.repository.AccountRepository

class TransferUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(fromId: Int, toId: Int, amount: Double) =
        repository.transfer(fromId, toId, amount)
}