package com.example.skillshare.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Configure a Json instance to be lenient about unknown keys
    private val json = Json { ignoreUnknownKeys = true }

    // Create a logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Create a custom OkHttpClient and add the interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: SkillApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Add the custom client to Retrofit
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(SkillApiService::class.java)
    }
}