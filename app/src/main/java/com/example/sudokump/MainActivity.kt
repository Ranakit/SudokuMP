package com.example.sudokump

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.navigation.compose.rememberNavController
import com.example.sudokump.screens.NavigationComponent
import com.example.sudokump.ui.theme.SudokuMPTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sudokuMP : SudokuMP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
            val isDarkInit = sharedPreferences.getBoolean("isDark", isSystemInDarkTheme())
            val id = sharedPreferences.getInt("id", -1)
            sudokuMP.isDark.value = isDarkInit
            SudokuMPTheme(darkTheme = sudokuMP.isDark.value) {
                NavigationComponent(sharedPreferences, sudokuMP, navController)
            }
        }
    }

    val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
    @Provides
    fun provideSharedPreferences(): SharedPreferences{
        return this.sharedPreferences
    }
}