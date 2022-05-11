package com.example.healthassistant.data.experiences

import androidx.room.*
import com.example.healthassistant.data.experiences.entities.Experience
import kotlinx.coroutines.flow.Flow


@Dao
interface ExperienceDao {

    @Query("SELECT * from experience")
    fun getExperiences():
            Flow<List<Experience>>

    @Query("SELECT * from experience where id =:id")
    suspend fun getExperienceByID(id: String): Experience

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(experience: Experience)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(experience: Experience)

    @Query("DELETE from experience")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteExperience(experience: Experience)


}
