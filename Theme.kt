package com.fabrizio.sudoku.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

/* Color defined here in this file.kt will be automatically activated and used
  by both light and dark theme
 */
private val LightColorPalette = lightColors(
    primary = primaryGreen ,
    secondary = textColorLight,
    surface = lightGrey,
    primaryVariant = gridLineColorLight,
    onPrimary = accentAmber,
    onSurface = accentAmber

)

private val DarkColorPalette = darkColors(
    primary = primaryCharcoal,
    secondary = textColorDark,
    surface = lightGreyAlpha,
    primaryVariant = gridLineColorLight,
    onPrimary = accentAmber,
    onSurface = accentAmber
)


@Composable

/*
The first argument returns a boolean whether the user is using the app in dark or light
mode.
The second parameter , content , represents every object and action that will be wrapped inside
this theme.
This means that everything we put inside this composable will have access to all the colors , theme , ecc..
 */
fun SudokuTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {


    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = typography,
        shapes = shapes,
        content = content
    )
}