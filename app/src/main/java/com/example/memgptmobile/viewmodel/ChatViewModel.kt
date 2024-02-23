package com.example.memgptmobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memgptmobile.api.ApiKeyRequest
import com.example.memgptmobile.api.ChatRequest
import com.example.memgptmobile.api.MemGPTApiService
import com.example.memgptmobile.api.RetrofitManager
import com.example.memgptmobile.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

enum  class MessageType {
    USER,
    AI,
    AI_THOUGHT
}

data class Message(val content: String, val type: MessageType, val id: String = UUID.randomUUID().toString())

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val apiService: MemGPTApiService,
    private val settingsRepository: SettingsRepository,
    private val retrofitManager: RetrofitManager
) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<Message>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()

    fun authenticateUser() {
        Log.d("ChatViewModel", "Attempting to authenticate user...")
        viewModelScope.launch {
            try {
                val response = retrofitManager.apiService.authenticateUser()
                if (response.isSuccessful && response.body() != null) {
                    // Handle successful authentication
                    val authResponse = response.body()!!
                    Log.d("AuthTest", "UUID: ${authResponse.uuid}")
                } else {
                    // Handle API error
                    Log.d("AuthTest", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // Handle network or other errors
                Log.e("AuthTest", "Exception", e)
            }
        }
    }
    fun sendMessage(message: String) {
        Log.d("ChatViewModel", "Sending message...")
        _chatMessages.value = _chatMessages.value + Message(content = message, type = MessageType.USER)
        // Fetch user and agent IDs from the settings repository
        val userId = settingsRepository.getUserID()
        val agentId = settingsRepository.getAgentID()

        val request = ChatRequest(
            agent_id = agentId,
            message = message,
            role = "user",
            stream = false
        )

        viewModelScope.launch {
            val apiKey = settingsRepository.getApiKey()
            if (apiKey.isNullOrEmpty()){
                //send warning toast
                try {
                    val userID = settingsRepository.getUserID()
                    val apiKeyRequest = ApiKeyRequest(userID = userID)
                    val apiKeyResponse = retrofitManager.apiService.getApiKey(apiKeyRequest)
                    if (apiKeyResponse.isSuccessful && apiKeyResponse.body() != null) {
                        settingsRepository.saveApiKey(apiKeyResponse.body()!!.apiKey)
                        // Proceed with sending the message now that the API key has been set
                        sendActualMessage(message)
                    } else {
                        Log.e("ChatViewModel", "Failed to retrieve API Key")
                        // Handle the failure to retrieve the API key (e.g., show an error to the user)
                    }
                } catch (e: Exception) {
                    Log.e("ChatViewModel", "Error retrieving API Key", e)
                    // Handle network or other errors
                }
            } else {
                sendActualMessage(message)
            }
        }
    }

    private suspend fun sendActualMessage(message: String) {
        // Existing logic to prepare and send the message
        val agentId = settingsRepository.getAgentID()

        val request = ChatRequest(
            agent_id = agentId,
            message = message,
            role = "user",
            stream = false
        )

        val response = retrofitManager.apiService.sendMessage(payload = request)
        if (response.isSuccessful && response.body() != null) {
            Log.d("ApiResponse", response.body().toString())
            response.body()!!.messages.forEach { content ->
                content.internalMonologue?.let {
                    _chatMessages.value = _chatMessages.value + Message(it, MessageType.AI_THOUGHT)
                }
                content.assistantMessage?.let {
                    _chatMessages.value = _chatMessages.value + Message(it, MessageType.AI)
                }
            }
        } else {
            // Consider adding error handling or logging here
            Log.e("ChatViewModel", "Failed to send message")
            // Here you could update the UI to reflect the failure to send the message
        }
    }
}