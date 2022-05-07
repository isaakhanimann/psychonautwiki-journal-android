package com.example.healthassistant.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDatabaseDao {

    @Query("SELECT * from note")
    fun getNotes():
            Flow<List<Note>>

    @Query("SELECT * from note where id =:id")
    suspend fun getNoteById(id: String): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note: Note)

    @Query("DELETE from note")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteNote(note: Note)


}
