package com.astute.data.repository.activity

import com.astute.data.models.Activity
import com.astute.util.Constants

interface ActivityRepository {

    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<Activity>

    suspend fun createActivity(activity: Activity)

    suspend fun deleteActivity(activityId: String): Boolean
}