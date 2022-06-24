package com.example.healthassistant.data.room.experiences

import androidx.room.*
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface ExperienceDao {

    @Query("SELECT * FROM experience ORDER BY creationDate DESC")
    fun getExperiences(): Flow<List<Experience>>

    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getIngestionsSortedDescending(): Flow<List<Ingestion>>

    @Query("SELECT * FROM ingestion WHERE time > :date ORDER BY time ASC")
    suspend fun getIngestionAfterDate(date: Date): List<Ingestion>

    @Query("SELECT DISTINCT substanceName FROM ingestion ORDER BY time DESC LIMIT :limit")
    fun getLastUsedSubstanceNames(limit: Int): Flow<List<String>>

    @Transaction
    @Query("SELECT DISTINCT e.id, e.title, e.creationDate, e.text FROM experience AS e LEFT JOIN ingestion AS i ON e.id == i.experienceId ORDER BY case when i.time IS NULL then e.creationDate else i.time end DESC")
    fun getSortedExperiencesWithIngestions(): Flow<List<ExperienceWithIngestions>>

    @Query("SELECT * FROM experience ORDER BY creationDate DESC LIMIT :limit")
    suspend fun getLastExperiences(limit: Int): List<Experience>

    @Query("SELECT * FROM experience WHERE id =:id")
    suspend fun getExperienceByID(id: Int): Experience?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(experience: Experience): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(experience: Experience)

    @Delete
    suspend fun deleteExperience(experience: Experience)

    @Delete
    suspend fun deleteIngestion(ingestion: Ingestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingestion: Ingestion)

    @Transaction
    @Query("SELECT * FROM experience WHERE id = :experienceId")
    fun getExperienceWithIngestions(experienceId: Int): Flow<ExperienceWithIngestions?>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC LIMIT 1")
    suspend fun getLastIngestion(substanceName: String): Ingestion?
}
