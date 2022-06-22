package com.example.sudokump.domain

import androidx.compose.ui.text.android.selection.WordBoundary

/*
Class for defining the general settings for the sudoku board
 */
data class Settings(
    val difficulty: Difficulty,
    val boundary: Int
)
