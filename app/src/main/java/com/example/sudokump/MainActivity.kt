package com.example.sudokump

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.navigation.compose.rememberNavController
import com.example.sudokump.common.makeToast
import com.example.sudokump.screens.NavigationComponent
import com.example.sudokump.ui.theme.SudokuMPTheme
import com.example.sudokump.ui.theme.activeGame.ActiveGameContainer
import com.example.sudokump.ui.theme.activeGame.ActiveGameLogic
import com.example.sudokump.ui.theme.activeGame.buildlogic.buildActiveGameLogic
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity(),ActiveGameContainer {

    @Inject
    lateinit var sudokuMP : SudokuMP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
            val isDarkInit = sharedPreferences.getBoolean("isDark", isSystemInDarkTheme())
            sharedPreferences.getInt("id", -1)
            sudokuMP.isDark.value = isDarkInit
            SudokuMPTheme(darkTheme = sudokuMP.isDark.value) {
                NavigationComponent(sharedPreferences, sudokuMP, navController, this)
            }
        }
    }

    override fun showErr() {
        makeToast(getString(R.string.generic_error))
    }

    override fun onNewGameClick() {

    }
}