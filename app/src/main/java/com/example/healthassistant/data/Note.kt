package com.example.healthassistant.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes_tbl" )
data class Note(
     @PrimaryKey
      val id: UUID = UUID.randomUUID(),

     @ColumnInfo(name = "note_title" )
      val title: String,

     @ColumnInfo(name = "note_description")
      val description: String,

               )
