package com.astute.controller.user

import com.astute.data.models.User

interface UserController {

    suspend fun createUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?

}