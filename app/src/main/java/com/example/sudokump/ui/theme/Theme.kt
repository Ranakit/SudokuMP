package com.example.sudokump.ui.theme

import android.content.res.Resources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
        primary = Purple500,
        primaryVariant = Purple700,
        secondary = Color.Gray,
        background = Color.Black,
        onBackground = Color.White
)

private val LightColorPalette = lightColors(
        primary = Purple500,
        primaryVariant = Purple700,
        secondary = Teal200,
        background = Color.White,
        onBackground = Color.Black
)

@Composable
fun SudokuMPTheme(darkTheme: Boolean = isSystemInDarkTheme() , content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}