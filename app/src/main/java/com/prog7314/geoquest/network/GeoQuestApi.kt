package com.prog7314.geoquest.network

import com.prog7314.geoquest.models.Issue
import retrofit2.Response
import retrofit2.http.*

interface GeoQuestApi {
    @GET("issues")
    suspend fun getIssues(): List<Issue>

    @POST("issues")
    suspend fun reportIssue(@Body issue: Issue): Response<Unit>
}