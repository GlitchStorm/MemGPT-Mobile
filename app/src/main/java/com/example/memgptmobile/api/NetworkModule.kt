package com.example.memgptmobile.api

import android.content.Context
import com.example.memgptmobile.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(settingsRepository: SettingsRepository): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        } // Assuming you have a reference to the settings repository
        val bearerTokenInterceptor = BearerTokenInterceptor(settingsRepository) // Assuming you have a reference to the settings repository
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(bearerTokenInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Adjust timeouts as necessary
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideMemGPTApiService(retrofitManager: RetrofitManager): MemGPTApiService {
        // This assumes RetrofitManager has a method or property to get the current MemGPTApiService instance
        return retrofitManager.apiService
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    // Other shared network components (like custom converters or interceptors) can be provided here.
}
