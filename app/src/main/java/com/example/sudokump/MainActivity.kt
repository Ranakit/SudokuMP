package com.example.sudokump

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
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
class MainActivity: ComponentActivity() {

    @Inject
    lateinit var sudokuMP : SudokuMP

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val isDarkInit = sharedPreferences.getBoolean("isDark", isSystemInDarkTheme())
            sharedPreferences.getInt("id", -1)
            sudokuMP.isDark.value = isDarkInit
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            SudokuMPTheme(darkTheme = sudokuMP.isDark.value) {
                NavigationComponent(sharedPreferences, sudokuMP, navController)
            }
        }
    }
}