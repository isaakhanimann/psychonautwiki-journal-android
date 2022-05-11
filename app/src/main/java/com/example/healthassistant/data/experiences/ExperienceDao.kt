package com.example.healthassistant.data.experiences

import androidx.room.*
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.experiences.entities.Ingestion
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface ExperienceDao {

    @Query("SELECT * FROM experience")
    fun getExperiences():
            Flow<List<Experience>>

    @Query("SELECT * FROM experience WHERE id =:id")
    suspend fun getExperienceByID(id: String): Experience

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(experience: Experience)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(experience: Experience)

    @Query("DELETE FROM experience")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteExperience(experience: Experience)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingestion: Ingestion)

    @Transaction
    @Query("SELECT * FROM experience WHERE id = :experienceId")
    suspend fun getExperienceWithIngestions(experienceId: UUID): List<ExperienceWithIngestions>
}
