package com.example.memgptmobile.api

import com.example.memgptmobile.repository.SettingsRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BearerTokenInterceptor @Inject constructor(private val settingsRepository: SettingsRepository) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = settingsRepository.getBearerToken() // Assuming you have a method to get the token
        val newRequest = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
