package com.astute.di

import com.astute.data.repository.comment.CommentRepository
import com.astute.data.repository.comment.CommentRepositoryImpl
import com.astute.data.repository.follow.FollowRepository
import com.astute.data.repository.follow.FollowRepositoryImpl
import com.astute.data.repository.likes.LikeRepository
import com.astute.data.repository.likes.LikeRepositoryImpl
import com.astute.data.repository.post.PostRepository
import com.astute.data.repository.post.PostRepositoryImpl
import com.astute.data.repository.user.UserRepository
import com.astute.data.repository.user.UserRepositoryImpl
import com.astute.service.*
import com.astute.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }
    single<LikeRepository> {
        LikeRepositoryImpl(get())
    }
    single { UserService(get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
    single { LikeService(get()) }
    single { CommentService(get()) }
}