package com.astute.data.repository.post

import com.astute.data.models.Post
import com.astute.data.responses.PostResponse
import com.astute.util.Constants


interface PostRepository {

    suspend fun createPost(post: Post): Boolean

    suspend fun deletePost(postId: String)

    suspend fun getPostsByFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post>

    suspend fun getPostsForProfile(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<PostResponse>

    suspend fun getPost(postId: String): Post?

    suspend fun getPostDetails(userId: String, postId: String): PostResponse?

}