package com.astute.data.repository.comment

import com.astute.data.models.Comment
import com.astute.data.responses.CommentResponse

interface CommentRepository {

    suspend fun createComment(comment: Comment): String

    suspend fun deleteComment(commentId: String): Boolean

    suspend fun deleteCommentsFromPost(postId: String): Boolean

    suspend fun getCommentsForPost(postId: String, ownUserId: String): List<CommentResponse>

    suspend fun getComment(commentId: String): Comment?
}