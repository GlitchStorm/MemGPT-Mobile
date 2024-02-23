package com.example.memgptmobile.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SettingsRepository @Inject constructor (private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    private val _apiBaseUrl = MutableStateFlow("http://localhost:8283")
    val apiBaseUrl: StateFlow<String> = _apiBaseUrl.asStateFlow()

    fun saveShowInternalMonologue(show: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean("SHOW_INTERNAL_MONOLOGUE", show)
            apply()
        }
    }
    fun getShowInternalMonologue(): Boolean {
        return sharedPreferences.getBoolean("SHOW_INTERNAL_MONOLOGUE", true)
    }
    fun saveApiSettings(ipAddress: String, port: String, userID: String, agentID: String) {
        Log.d("SettingsRepo", "Saving API Settings: IP=$ipAddress, Port=$port")
        sharedPreferences.edit().apply {
            putString("API_IP_ADDRESS", ipAddress)
            putString("API_PORT", port)
            putString("USER_ID", userID)
            putString("AGENT_ID", agentID)
            apply()
            _apiBaseUrl.value = "http://$ipAddress:$port"
            Log.d("SettingsRepo", "After Saving API Settings: New Base URL=${_apiBaseUrl.value}")
        }
    }

    fun saveApiKey(apiKey: String) {
        Log.d("SettingsRepo", "Saving API Key")
        sharedPreferences.edit().apply {
            putString("API_KEY", apiKey)
            apply()
        }
    }
    fun getApiBaseUrl(): String {
        val ipAddress = sharedPreferences.getString("API_IP_ADDRESS", "localhost") ?: "localhost"
        val port = sharedPreferences.getString("API_PORT", "8283") ?: "8283"
        val url = "http://$ipAddress:$port/api/"
        Log.d("SettingsRepo", "Retrieving API Base URL: $url")
        return url
    }

    fun getIpAddress(): String = sharedPreferences.getString("API_IP_ADDRESS", "default_ip") ?: "default_ip"
    fun getPort(): String = sharedPreferences.getString("API_PORT", "default_port") ?: "default_port"
    fun getUserID(): String = sharedPreferences.getString("USER_ID", "00000000-0000-0000-0000-cc4740043c00") ?: "default_user_id"
    fun getAgentID(): String = sharedPreferences.getString("AGENT_ID", "be0a6165-eaa6-484e-87e0-994df8414cfe") ?: "default_agent_id"
    fun getBearerToken(): String = sharedPreferences.getString("BEARER_TOKEN", "testpassword") ?: "default_bearer_token"
    fun getApiKey(): String? = sharedPreferences.getString("API_KEY", null)
}