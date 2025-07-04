package com.carlosmagno.mywallet.domain.repository

import com.carlosmagno.mywallet.data.local.TransactionEntity

interface TransactionRepository {
    suspend fun insert(transaction: TransactionEntity)
    suspend fun getAll(): List<TransactionEntity>
    suspend fun getAllByUser(userId: Int): List<TransactionEntity>  // novo método
}