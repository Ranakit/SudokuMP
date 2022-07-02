package com.example.sudokump.domain

import com.example.sudokump.computationlogic.buildNewSudoku
import com.example.sudokump.model.Difficulties
import java.io.Serializable
import java.util.*
import kotlin.collections.LinkedHashMap

data class SudokuPuzzle(
    val boundary : Int,
    val difficulty : Difficulties,
    val graph: LinkedHashMap<Int, LinkedList<SudokuNode>>
    = buildNewSudoku(boundary, difficulty).graph,
    var elapsedTime : Long = 0L
): Serializable {
    fun getValue(): LinkedHashMap<Int , LinkedList<SudokuNode>> = graph

}
