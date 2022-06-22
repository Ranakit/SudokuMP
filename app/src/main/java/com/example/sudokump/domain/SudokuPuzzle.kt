package com.fabrizio.sudoku.domain

import java.io.Serializable
import java.util.*
import kotlin.collections.LinkedHashMap

data class SudokuPuzzle(
    val boundary : Int,
    val difficulty : Difficulty, = buildNewSudoku(boundary , difficulty).graph,
    var elapsedTime : Long = 0L
): Serializable {
    fun getValue(): LinkedHashMap<Int , LinkedList<SudokuNode>> = graph

}
