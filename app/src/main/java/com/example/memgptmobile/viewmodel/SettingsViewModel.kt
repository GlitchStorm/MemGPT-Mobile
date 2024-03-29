package com.example.memgptmobile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.memgptmobile.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val settingsRepository: SettingsRepository
) : ViewModel() {
    fun saveShowInternalMonologue(show: Boolean) {
        settingsRepository.saveShowInternalMonologue(show)
    }

    fun getShowInternalMonologue(): Boolean {
        return settingsRepository.getShowInternalMonologue()
    }
}