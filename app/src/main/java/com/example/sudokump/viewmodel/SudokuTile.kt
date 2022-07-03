package com.example.sudokump.viewmodel

import androidx.compose.runtime.MutableState

class SudokuTile(
    val x: MutableState<Int>,
    val y: MutableState<Int>,
    var value: MutableState<Int>,
    var isCorrect: MutableState<Boolean>,
    val readOnly: Boolean
)