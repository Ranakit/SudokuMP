package com.example.sudokump

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ThemeSwitcher : Application(){
    var isDark = mutableStateOf(true)
    fun themeSwitch() {
        isDark.value = !isDark.value
    }
}