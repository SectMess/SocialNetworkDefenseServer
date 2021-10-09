package com.astute.util

object ApiResponseMessages {
    const val COMMENT_TOO_LONG = "The comment length must not exceed ${Constants.MAX_COMMENT_LENGTH} characters."
    const val USER_ALREADY_EXISTS = "A user with this email already exists."
    const val INVALID_CREDENTIALS = "Incorrect Login. Please try again."
    const val FIELDS_BLANK = "The fields my not be empty."
    const val USER_NOT_FOUND = "User not found."

}