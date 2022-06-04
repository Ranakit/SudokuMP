package com.example.sudokump

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sudokump.screens.NavigationComponent
import com.example.sudokump.ui.theme.SudokuMPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SudokuMPTheme {
                NavigationComponent(navController)
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Light Mode"
)

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun DefaultPreview() {
    SudokuMPTheme {
        val navController = rememberNavController()
        NavigationComponent(navController = navController)
    }
}

