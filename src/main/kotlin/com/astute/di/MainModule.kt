package com.astute.di

import com.astute.data.models.Skill
import com.astute.data.repository.activity.ActivityRepository
import com.astute.data.repository.activity.ActivityRepositoryImpl
import com.astute.data.repository.comment.CommentRepository
import com.astute.data.repository.comment.CommentRepositoryImpl
import com.astute.data.repository.follow.FollowRepository
import com.astute.data.repository.follow.FollowRepositoryImpl
import com.astute.data.repository.likes.LikeRepository
import com.astute.data.repository.likes.LikeRepositoryImpl
import com.astute.data.repository.post.PostRepository
import com.astute.data.repository.post.PostRepositoryImpl
import com.astute.data.repository.skill.SkillRepository
import com.astute.data.repository.skill.SkillRepositoryImpl
import com.astute.data.repository.user.UserRepository
import com.astute.data.repository.user.UserRepositoryImpl
import com.astute.service.*
import com.astute.util.Constants
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.insertOne
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
//            .also {
//            val skills = it.getCollection<Skill>()
//            GlobalScope.launch {
//                skills.insertOne(Skill(name="Kotlin", imageUrl = "http://10.0.2.2:8881/skills/Kotlin-logo.svg"))
//
//            }
//
//
//        }
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
    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }
    single<SkillRepository> {
        SkillRepositoryImpl(get())
    }
    single { UserService(get(), get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
    single { LikeService(get(), get(), get()) }
    single { CommentService(get(), get()) }
    single { ActivityService(get(), get(), get()) }
    single { SkillService(get()) }


    single { Gson() }
}