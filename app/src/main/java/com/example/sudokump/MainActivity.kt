package com.example.sudokump

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.navigation.compose.rememberNavController
import com.example.sudokump.screens.NavigationComponent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sudokump.screens.CompletedGamesScreen
import com.example.sudokump.screens.SavedGamesScreen
import com.example.sudokump.ui.theme.SudokuMPTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import dagger.hilt.android.AndroidEntryPoint

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
            SudokuMPTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    SavedGamesScreen()
                    //CompletedGamesScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SudokuMPTheme {
        Greeting("Android")
    }
}