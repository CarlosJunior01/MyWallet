package com.carlosmagno.mywallet.data.repositoryImpl

import com.carlosmagno.mywallet.data.local.TransactionDao
import com.carlosmagno.mywallet.data.local.TransactionEntity
import com.carlosmagno.mywallet.domain.repository.TransactionRepository

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun insert(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    override suspend fun getAll(): List<TransactionEntity> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun getAllByUser(userId: Int): List<TransactionEntity> {
        return transactionDao.getTransactionsByUser(userId)
    }
}