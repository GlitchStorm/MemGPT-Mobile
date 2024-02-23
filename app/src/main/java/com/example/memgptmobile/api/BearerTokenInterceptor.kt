package com.example.memgptmobile.api

import com.example.memgptmobile.repository.SettingsRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BearerTokenInterceptor @Inject constructor(private val settingsRepository: SettingsRepository) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val isRequestForAdmin = request.url.encodedPath.startsWith("/admin")
        val authValue = if (isRequestForAdmin) {
            val token = settingsRepository.getBearerToken() // Assuming you have a method to get the bearer token
            "Bearer $token"
        } else {
            val apiKey = settingsRepository.getApiKey() // Assuming you have a method to get the API key
            "Bearer $apiKey" // Adjust the header format as needed
        }
        val newRequest = request.newBuilder()
            .header("Authorization", authValue)
            .build()
        return chain.proceed(newRequest)
    }
}
