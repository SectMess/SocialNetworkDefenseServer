package com.astute.routes

import com.astute.data.models.Post
import com.astute.data.repository.post.PostRepository
import com.astute.data.requests.CreatePostRequest
import com.astute.data.responses.BasicApiResponse
import com.astute.service.PostService
import com.astute.service.UserService
import com.astute.util.ApiResponseMessages
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createPostRoute(
    postService: PostService,
    userService: UserService
) {
    authenticate {
        post("/api/post/create") {
            val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val email = call.principal<JWTPrincipal>()?.getClaim("email", String::class)
            val isEmailByUser = userService.doesEmailBelongToUserId(
                email = email ?: "",
                userId = request.userId
            )
            if(!isEmailByUser) {
                call.respond(HttpStatusCode.Unauthorized, "Unauthorized.")
                return@post
            }

            val didUserExist = postService.createPostIfUserExists(request)
            if (!didUserExist) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                    )
                )
            }
        }
    }
}