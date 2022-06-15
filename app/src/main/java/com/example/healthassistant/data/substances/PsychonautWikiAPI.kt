package com.example.healthassistant.data.substances

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PsychonautWikiAPI {
    @Headers("Content-Type: application/json")
    @POST("/")
    suspend fun postDynamicQuery(@Body body: String): Response<String>
}