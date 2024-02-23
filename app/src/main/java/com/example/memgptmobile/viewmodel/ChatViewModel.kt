package com.example.memgptmobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            userID = userId,
            agentID = agentId,
            message = message,
            role = "user",
            stream = false
        )

        viewModelScope.launch {
            val response = retrofitManager.apiService.sendMessage()
            if (response.isSuccessful && response.body() != null) {
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
            }
        }
    }
}