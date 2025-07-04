package com.carlosmagno.mywallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountEntity(
    @PrimaryKey val userId: Int,
    val balance: Double
)