package com.astute.di

import com.astute.controller.user.UserController
import com.astute.controller.user.UserControllerImpl
import com.astute.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }
    single<UserController> {
        UserControllerImpl(get())
    }
}