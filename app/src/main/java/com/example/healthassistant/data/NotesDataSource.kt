package com.example.healthassistant.data

class NotesDataSource {
    fun loadNotes(): List<Note> {
        return listOf(
            Note(title = "A good day"),
            Note(title = "Android Compose"),
            Note(title = "Keep at it..."),
            Note(title = "A movie day"),
            Note(title = "A movie day"),
            Note(title = "A movie day"),
            Note(title = "A movie day"),
            Note(title = "A movie day"),
            Note(title = "A movie day"),
            Note(title = "A movie day")
        )
    }
}