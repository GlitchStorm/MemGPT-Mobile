package com.example.memgptmobile.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.Response
import com.example.memgptmobile.viewmodel.SettingsViewModel

interface MemGPTApiService {
    @POST("api/agents/message") // Use the correct endpoint
    @Headers(
        "accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun sendMessage(
        @Body payload: ChatRequest
    ): Response<ChatResponse>

    @GET("api/auth") // Use the correct endpoint
    suspend fun authenticateUser(): Response<AuthResponse>

    @POST("admin/users/keys")
    @Headers(
        "accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getApiKey(
        @Body payload: ApiKeyRequest
    ): Response<ApiKeyResponse>
}