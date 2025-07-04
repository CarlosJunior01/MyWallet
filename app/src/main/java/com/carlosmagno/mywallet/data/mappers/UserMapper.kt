package com.carlosmagno.mywallet.data.mappers

import com.carlosmagno.mywallet.data.local.UserEntity
import com.carlosmagno.mywallet.domain.model.User

fun UserEntity.toDomain(): User = User(
    id = this.id,
    name = this.name,
    email = this.email
)

fun User.toEntity(password: String): UserEntity = UserEntity(
    id = this.id,
    name = this.name,
    email = this.email,
    password = password
)