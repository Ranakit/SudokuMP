package com.example.sudokump.common

import com.example.sudokump.model.SudokuNode
import com.example.sudokump.model.getHash
import kotlin.math.sqrt

internal val Int.sqrt: Int
    get() = sqrt(this.toDouble()).toInt()


fun mapToList (board: Map<Int, SudokuNode>) : List<List<Int>>{
    val list = mutableListOf<List<Int>>()
    for (i in (0..9)) {

        val list1 = mutableListOf<Int>()
        for (j in(0..9)){

            val value = if ( board[getHash(i,j)] != null) board[getHash(i,j)]?.getValue() else 0
            if (value != null) {
                list1.add(value)
            }
        }
        list.add(list1)
    }
    return list
}