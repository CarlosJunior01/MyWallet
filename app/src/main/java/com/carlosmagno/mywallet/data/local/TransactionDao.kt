package com.carlosmagno.mywallet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM TransactionEntity ORDER BY timestamp DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM TransactionEntity WHERE fromUserId = :userId OR toUserId = :userId ORDER BY timestamp DESC")
    suspend fun getTransactionsByUser(userId: Int): List<TransactionEntity>

}
