package com.example.healthassistant.data.experiences

import com.example.healthassistant.data.experiences.entities.Experience
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExperienceRepository @Inject constructor(private val experienceDao: ExperienceDao) {
    suspend fun addExperience(experience: Experience) = experienceDao.insert(experience)
    suspend fun updateExperience(experience: Experience) = experienceDao.update(experience)
    suspend fun deleteExperience(experience: Experience) = experienceDao.deleteExperience(experience)
    suspend fun deleteAllExperiences() = experienceDao.deleteAll()
    fun getAllExperiences(): Flow<List<Experience>> = experienceDao.getExperiences().flowOn(Dispatchers.IO)
        .conflate()

}