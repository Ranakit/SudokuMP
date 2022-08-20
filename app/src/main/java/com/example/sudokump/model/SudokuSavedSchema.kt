package com.example.sudokump.model

import com.example.sudokump.common.MutablePair

data class SudokuSavedSchema (
    val Schema : List<List<MutablePair<Int, Int>>>,
    val Notes : List<List<List<Int>>>
)