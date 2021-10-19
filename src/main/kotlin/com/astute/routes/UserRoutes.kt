package com.astute.routes

import com.astute.data.requests.UpdateProfileRequest
import com.astute.data.responses.BasicApiResponse
import com.astute.data.responses.UserResponseItem
import com.astute.service.PostService
import com.astute.service.UserService
import com.astute.util.ApiResponseMessages
import com.astute.util.Constants
import com.astute.util.Constants.BANNER_IMAGE_PATH
import com.astute.util.Constants.BASE_URL
import com.astute.util.Constants.PROFILE_PICTURE_PATH
import com.astute.util.QueryParams
import com.astute.util.save
import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import java.io.File

//fun Route.createUser(
//    userService: UserService
//) {
//    post("/api/user/create") {
//        val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
//            call.respond(HttpStatusCode.BadRequest)
//            return@post
//        }
//        if (userService.doesUserWithEmailExist(request.email)) {
//            call.respond(
//                BasicApiResponse<T>(
//                    successful = false,
//                    message = USER_ALREADY_EXISTS
//                )
//            )
//            return@post
//        }
//
//        when (userService.validateCreateAccountRequest(request)) {
//            is UserService.ValidationEvent.ErrorFieldEmpty -> {
//                call.respond(
//                    BasicApiResponse(
//                        successful = false,
//                        message = FIELDS_BLANK
//                    )
//                )
//            }
//            is UserService.ValidationEvent.Success -> {
//                userService.createUser(request)
//                call.respond(
//                    BasicApiResponse(successful = true)
//                )
//            }
//        }
//    }
//}
//
//fun Route.loginUser(
//    userService: UserService,
//    jwtIssuer: String,
//    jwtAudience: String,
//    jwtSecret: String,
//) {
//    post("/api/user/login") {
//        val request = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
//            call.respond(HttpStatusCode.BadRequest)
//            return@post
//        }
//
//        if (request.email.isBlank() || request.password.isBlank()) {
//            call.respond(HttpStatusCode.BadRequest)
//            return@post
//        }
//
//        val user = userService.getUserByEmail(request.email) ?: kotlin.run {
//            call.respond(
//                HttpStatusCode.OK,
//                BasicApiResponse(
//                    successful = false,
//                    message = INVALID_CREDENTIALS
//                )
//            )
//            return@post
//        }
//        val isCorrectPassword = userService.isValidPassword(
//            enteredPassword = request.password,
//            actualPassword = user.password
//        )
//
//
//        if(isCorrectPassword) {
//            val expiresIn = 1000L * 60L * 60L * 24L * 365L
//            val token = JWT.create()
//                .withClaim("userId", user.id)
//                .withIssuer(jwtIssuer)
//                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
//                .withAudience(jwtAudience)
//                .sign(Algorithm.HMAC256(jwtSecret))
//            call.respond(
//                HttpStatusCode.OK,
//                AuthResponse(token = token)
//            )
//        } else {
//            call.respond(
//                HttpStatusCode.OK,
//                BasicApiResponse<Unit>(
//                    successful = false,
//                    message = INVALID_CREDENTIALS
//                )
//            )
//        }
//    }
//}

fun Route.searchUser(userService: UserService) {
    authenticate {
        get("/api/user/search") {
            val query = call.parameters[QueryParams.PARAM_QUERY]
            if (query == null || query.isBlank()) {
                call.respond(
                    HttpStatusCode.OK,
                    listOf<UserResponseItem>()
                )
                return@get
            }
            val searchResults = userService.searchForUsers(query, call.userId)
            call.respond(
                HttpStatusCode.OK,
                searchResults
            )
        }
    }
}

fun Route.getUserProfile(userService: UserService) {
    authenticate {
        get("/api/user/profile") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID]
            if (userId == null || userId.isBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val profileResponse = userService.getUserProfile(userId, call.userId)
            if (profileResponse == null) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
                return@get
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = profileResponse
                )
            )
            println(profileResponse.topSkills)
        }
    }
}

fun Route.updateUserProfile(userService: UserService) {
    val gson: Gson by inject()
    authenticate {
        put("/api/user/update") {
            val multipart = call.receiveMultipart()
            var updateProfileRequest: UpdateProfileRequest? = null
            var profilePictureFileName: String? = null
            var bannerImageFileName: String? = null
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "update_profile_data") {
                            updateProfileRequest = gson.fromJson(
                                partData.value,
                                UpdateProfileRequest::class.java
                            )
                        }
                    }
                    is PartData.FileItem -> {
                        if (partData.name == "profile_picture") {
                            profilePictureFileName = partData.save(PROFILE_PICTURE_PATH)
                        } else if (partData.name == "banner_pictures") {
                            bannerImageFileName = partData.save(BANNER_IMAGE_PATH)
                        }
                    }
                    is PartData.BinaryItem -> Unit
                }
            }

            val profilePictureUrl = "${BASE_URL}profile_pictures/$profilePictureFileName"
            val bannerImageUrl = "${BASE_URL}banner_pictures/$bannerImageFileName"

            updateProfileRequest?.let { request ->
                val updateAcknowledged = userService.updateUser(
                    userId = call.userId,
                    profileImageUrl = if (profilePictureFileName == null) {
                        null
                    } else {
                        profilePictureUrl
                    },
                    bannerUrl = if(bannerImageFileName == null) {
                        null
                    } else {
                        bannerImageUrl
                    },
                    updateProfileRequest = request
                )
                if (updateAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                } else {
                    File("${PROFILE_PICTURE_PATH}/$profilePictureFileName").delete()
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
        }
    }
}
