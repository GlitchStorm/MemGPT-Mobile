package com.example.memgptmobile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.memgptmobile.api.MemGPTApiService
import com.example.memgptmobile.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val apiService: MemGPTApiService,
    val settingsRepository: SettingsRepository
) : ViewModel() {
    // Implement your ViewModel logic here, e.g., making API calls using apiService
}