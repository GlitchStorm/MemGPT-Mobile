package com.example.memgptmobile.api

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    val agent_id: String,
    val message: String,
    val role: String,
    val stream: Boolean
)

data class ChatResponse(
    val messages: List<MessageContent>
)

data class AuthResponse(
    @SerializedName("uuid") val uuid: String
)

data class ApiKeyResponse(
    @SerializedName("api_key") val apiKey: String
)

data class MessageContent(
    @SerializedName("internal_monologue") val internalMonologue: String? = null,
    @SerializedName("assistant_message") val assistantMessage: String? = null,
    val functionCall: String? = null,
    val functionReturn: String? = null,
    val status: String? = null
)

data class ApiKeyRequest(
    @SerializedName("user_id")
    val userID: String
)

class ApiKeyException(val errorType: ErrorType): Exception() {
    enum class ErrorType {
        MISSING_API_KEY,
        FAILED_TO_RETRIEVE_API_KEY
    }
}
