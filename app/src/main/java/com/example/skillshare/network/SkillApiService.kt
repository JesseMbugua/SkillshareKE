package com.example.skillshare.network

import com.example.skillshare.data.CreateSkillRequest
import com.example.skillshare.data.PresignRequest
import com.example.skillshare.data.PresignResponse
import com.example.skillshare.data.Skill
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SkillApiService {

    @POST("skills")
    suspend fun createSkill(
        @Body request: CreateSkillRequest
    ): Skill

    // Keep the same params you already had
    @GET("skills")
    suspend fun getSkills(
        @Query("trainerId") trainerId: String? = null,
        @Query("price") price: Double? = null,
        @Query("location") location: String? = null,
        @Query("email") email: String? = null,
        @Query("trainer") trainerName: String? = null,
        @Query("title") title: String? = null
    ): List<Skill>

    @GET("skills/{id}")
    suspend fun getSkill(@Path("id") id: String): Skill

    @PUT("skills/{id}")
    suspend fun updateSkill(
        @Path("id") id: String,
        @Body request: CreateSkillRequest
    ): Skill

    @DELETE("skills/{id}")
    suspend fun deleteSkill(@Path("id") id: String)

    @POST("presign")
    suspend fun getPresignedUrl(
        @Body request: PresignRequest
    ): PresignResponse

    @PUT
    suspend fun uploadVideo(
        @Url url: String,
        @Body video: RequestBody
    ): Response<Unit>
}
