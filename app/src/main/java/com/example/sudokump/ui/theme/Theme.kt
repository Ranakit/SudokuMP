package com.example.sudokump.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple500,
    secondary = Teal100,
    surface = lightGreyAlpha,
    primaryVariant = gridLineColorLight,
    onSecondary = readOnlyDark,
    onBackground = Teal100,
    secondaryVariant = RedD,
    error = YellowD,
    onError = GreenD,
    onPrimary = Gold,
    onSurface = Silver,
    background = Bronze
)

private val LightColorPalette = lightColors(
    primary = Purple200,
    secondary = Teal200,
    surface = lightGrey,
    primaryVariant = gridLineColorLight,
    onBackground = Color.Black,
    onSecondary = Purple200,
    secondaryVariant = Color.Red,
    error = Color.Yellow,
    onError = Color.Green,
    onPrimary = Gold,
    onSurface = Silver,
    background = Bronze
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