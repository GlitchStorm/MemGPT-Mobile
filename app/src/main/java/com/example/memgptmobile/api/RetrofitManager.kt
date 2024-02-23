package com.example.memgptmobile.api

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.memgptmobile.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

class RetrofitManager @Inject constructor(private val settingsRepository: SettingsRepository, private val okHttpClient: OkHttpClient) {
    private val apiServiceLock = Any()

    val apiService: MemGPTApiService
        get() = synchronized(apiServiceLock) {
           val currentBaseUrl = settingsRepository.apiBaseUrl.value
            Retrofit.Builder()
                .baseUrl(currentBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MemGPTApiService::class.java)
        }
}