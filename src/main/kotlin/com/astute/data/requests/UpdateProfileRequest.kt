package com.astute.data.requests

import com.astute.data.models.Skill
import com.astute.data.responses.SkillDto

data class UpdateProfileRequest(
    val username: String,
    val bio: String,
    val gitHubUrl: String,
    val instagramUrl: String,
    val linkedInUrl: String,
    val skills: List<SkillDto>,
)