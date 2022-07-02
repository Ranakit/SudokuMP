package com.example.sudokump.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = primaryGreen ,
    secondary = textColorLight,
    surface = lightGrey,
    primaryVariant = gridLineColorLight,
    onPrimary = accentAmber,
    onSurface = accentAmber,
    onBackground = Teal100,
    onSecondary = Color.DarkGray,
    secondaryVariant = RedD,
    error = YellowD,
    onError = GreenD
)

private val LightColorPalette = lightColors(
    primary = primaryCharcoal,
    secondary = textColorDark,
    surface = lightGreyAlpha,
    primaryVariant = gridLineColorLight,
    onPrimary = accentAmber,
    onSurface = accentAmber,
    onBackground = Color.Black,
    onSecondary = Purple200,
    secondaryVariant = Color.Red,
    error = Color.Yellow,
    onError = Color.Green
)

@Composable
fun SudokuMPTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
            colors = colors,
            typography = Typography(),
            shapes = Shapes,
            content = content
    )
}

@Composable
public fun AppToolbar(
    modifier: androidx.compose.ui.Modifier,
    title: String,
    icon: @Composable
        () -> Unit
) : Unit {
    return Unit
}