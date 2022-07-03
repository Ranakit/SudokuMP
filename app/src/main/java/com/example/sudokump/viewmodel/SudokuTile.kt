package com.example.sudokump.viewmodel

class SudokuTile(
    val x: Int,
    val y: Int,
    var value: Int,
    var hasFocus: Boolean,
    val readOnly: Boolean
)