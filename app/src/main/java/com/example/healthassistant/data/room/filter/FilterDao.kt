package com.example.healthassistant.data.room.filter

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterDao {

    @Query("SELECT * FROM substancefilter")
    fun getFilters(): Flow<List<SubstanceFilter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(substanceFilter: SubstanceFilter)

    @Delete
    suspend fun delete(substanceFilter: SubstanceFilter)

    @Query("DELETE FROM substancefilter")
    suspend fun deleteAll()
}
