package com.example.sudokump

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SudokuMP @Inject constructor(): Application(){
    var isDark = mutableStateOf(true)
    fun themeSwitch() {
        isDark.value = !isDark.value
    }
}