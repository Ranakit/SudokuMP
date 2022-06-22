package com.example.sudokump.domain

import java.io.Serializable

/*
We will use this class for represent a single node , that linked consequently will give us a complete sudokuboard.
For handling the nodes and their connections we will use a graph method , with a LinkedList structure.
Our node is represented as follow:
- a value or color , in this case is an integer numer ranging from 1 to 9;
- a list of relative x and y values that stands for the coordinates on the sudoku board
 */
data class SudokuNode(
    val x : Int,
    val y : Int,
    var color: Int = 0,
    var readOnly: Boolean = true      // Used as the clues numbers that are already on the board for easing the game

    // We have to override the hash function given by the data class

) : Serializable {
    override fun hashCode(): Int {
        return getHash(x, y)
    }
}

internal fun getHash(x:Int , y: Int): Int {
    val newX = x*100
    return "$newX$y".toInt()
}
