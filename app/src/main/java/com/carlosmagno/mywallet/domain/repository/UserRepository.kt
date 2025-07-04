package com.carlosmagno.mywallet.domain.repository

import com.carlosmagno.mywallet.domain.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): User?
    suspend fun register(user: User, password: String): Boolean
    suspend fun getUserById(id: Int): User?
    suspend fun getUserByEmail(email: String): User?
}