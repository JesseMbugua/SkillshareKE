package com.example.skillshare.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Replace with your actual base URL
    private const val BASE_URL = "https://your.api.base.url/"

    val api: SkillApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SkillApiService::class.java)
    }
}
