package com.example.memgptmobile.api

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    val userID: String,
    val agentID: String,
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

data class MessageContent(
    val internalMonologue: String? = null,
    val assistantMessage: String? = null,
    val functionCall: String? = null,
    val functionReturn: String? = null,
    val status: String? = null
)