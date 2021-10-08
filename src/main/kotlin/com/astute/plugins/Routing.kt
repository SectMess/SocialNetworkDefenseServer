package com.astute.plugins

import com.astute.data.repository.follow.FollowRepository
import com.astute.data.repository.post.PostRepository
import com.astute.data.repository.user.UserRepository
import com.astute.routes.*
import com.astute.service.FollowService
import com.astute.service.PostService
import com.astute.service.UserService
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        //User Routes
        createUserRoute(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        //Following Routes
        followUser(followService)
        unfollowUser(followService)

        //Post Routes
        createPostRoute(postService, userService)
    }
}
