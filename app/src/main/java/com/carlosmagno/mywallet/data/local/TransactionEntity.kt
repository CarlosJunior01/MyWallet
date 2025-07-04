package com.carlosmagno.mywallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fromUserId: Int,
    val toUserId: Int,
    val amount: Double,
    val timestamp: Long
)