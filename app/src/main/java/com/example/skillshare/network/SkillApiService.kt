package com.example.skillshare.network

import com.example.skillshare.data.CreateSkillRequest
import com.example.skillshare.data.PresignRequest
import com.example.skillshare.data.PresignResponse
import com.example.skillshare.data.Skill
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface SkillApiService {

    @POST("skills")
    suspend fun createSkill(@Body request: CreateSkillRequest): Skill

    @GET("skills")
    suspend fun getSkills(
        @Query("trainerId") trainerId: String? = null,
        @Query("price") price: Double? = null,
        @Query("location") location: String? = null,
        @Query("email") email: String? = null
    ): List<Skill>

    @GET("skills/{id}")
    suspend fun getSkill(@Path("id") id: String): Skill

    @PUT("skills/{id}")
    suspend fun updateSkill(@Path("id") id: String, @Body request: CreateSkillRequest): Skill

    @DELETE("skills/{id}")
    suspend fun deleteSkill(@Path("id") id: String)

    @POST("presign")
    suspend fun getPresignedUrl(@Body request: PresignRequest): PresignResponse

    @PUT
    suspend fun uploadVideo(@Url url: String, @Body video: RequestBody): Response<Unit>
}
