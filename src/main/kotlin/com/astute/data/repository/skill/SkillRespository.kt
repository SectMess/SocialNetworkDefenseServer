package com.astute.data.repository.skill

import com.astute.data.models.Skill

interface SkillRepository {

    suspend fun getSkills(): List<Skill>
}