package com.example.memgptmobile.debug

import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Logger {
    private lateinit var logDirectory: String

    fun initialize(directory: String) {
        logDirectory = directory
    }

    @Synchronized
    fun logToFile(tag: String, message: String) {
        val timestamp =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        val logMessage = "$timestamp $tag: $message\n"

        val logFile = File(logDirectory, "log.txt")

        try {
            FileWriter(logFile, true).use { writer ->
                writer.append(logMessage)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}