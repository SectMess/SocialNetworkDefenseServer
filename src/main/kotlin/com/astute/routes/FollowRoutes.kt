package com.astute.routes

import com.astute.data.requests.FollowUpdateRequest
import com.astute.data.responses.BasicApiResponse
import com.astute.service.FollowService
import com.astute.util.ApiResponseMessages.USER_NOT_FOUND
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.followUser(followService: FollowService) {
    authenticate {
        post("/api/following/follow") {
            val request = call.receiveOrNull<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val didUserExist = followService.followUserIfExists(request, call.userId)

            if(didUserExist) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = USER_NOT_FOUND
                    )
                )
            }
        }
    }
}

fun Route.unfollowUser(followService: FollowService) {
    delete("/api/following/unfollow") {
        val request = call.receiveOrNull<FollowUpdateRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        val didUserExist = followService.unfollowUserIfExists(request, call.userId)
        if(didUserExist) {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = false,
                    message = USER_NOT_FOUND
                )
            )
        }
    }
}