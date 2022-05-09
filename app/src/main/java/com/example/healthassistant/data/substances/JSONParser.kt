package com.example.healthassistant.data.substances

interface JSONParser<T> {
    suspend fun parse(string: String): List<T>
}