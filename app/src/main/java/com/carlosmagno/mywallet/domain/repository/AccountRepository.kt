package com.carlosmagno.mywallet.domain.repository

import com.carlosmagno.mywallet.data.local.AccountEntity

interface AccountRepository {
    suspend fun getAccountByUserId(userId: Int): AccountEntity?
    suspend fun updateAccount(account: AccountEntity)
    suspend fun createAccount(account: AccountEntity)
    suspend fun transfer(fromUserId: Int, toUserId: Int, amount: Double): Boolean
}