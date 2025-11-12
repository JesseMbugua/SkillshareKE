package com.example.skillshare.network

import com.example.skillshare.model.Skill
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SkillApiService {

    @POST("skills")
    suspend fun createSkill(@Body skill: Skill): Skill

    @GET("skills")
    suspend fun getSkills(): List<Skill>

    @GET("skills/{id}")
    suspend fun getSkill(@Path("id") id: String): Skill

    @PUT("skills/{id}")
    suspend fun updateSkill(@Path("id") id: String, @Body skill: Skill): Skill

    @DELETE("skills/{id}")
    suspend fun deleteSkill(@Path("id") id: String)
}
