package com.example.sudokump

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.navigation.compose.rememberNavController
import com.example.sudokump.screens.NavigationComponent
import com.example.sudokump.ui.theme.SudokuMPTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeSwitcher : ThemeSwitcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
            val isDarkInit = sharedPreferences.getBoolean("isDark", isSystemInDarkTheme())
            themeSwitcher.isDark.value = isDarkInit
            SudokuMPTheme(darkTheme = themeSwitcher.isDark.value) {
                NavigationComponent(sharedPreferences, themeSwitcher, navController)
            }
        }
    }
}

