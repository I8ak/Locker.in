package com.example.lockerin.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    var isLoading = mutableStateOf(false)
        private set

    fun setLoading(loading: Boolean) {
        isLoading.value = loading
    }
}
