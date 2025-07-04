package com.carlosmagno.mywallet.data.repositoryImpl

import com.carlosmagno.mywallet.data.local.UserDao
import com.carlosmagno.mywallet.data.local.UserEntity
import com.carlosmagno.mywallet.domain.model.User
import com.carlosmagno.mywallet.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun login(email: String, password: String): User? {
        val userEntity = userDao.login(email, password)
        return userEntity?.toUser()
    }

    override suspend fun register(user: User, password: String): Boolean {
        val userEntity = UserEntity(
            name = user.name,
            email = user.email,
            password = password
        )
        return userDao.insert(userEntity) > 0
    }

    override suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)?.toUser()
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.toUser()
    }

    private fun UserEntity.toUser() = User(
        id = id,
        name = name,
        email = email
    )
}