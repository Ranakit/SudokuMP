package com.example.sudokump.model

import java.io.Serializable

data class SudokuNode(
    val x : Int,
    val y : Int,
    var value: Int = 0,
    var readOnly: Boolean = true      // Used as the clues numbers that are already on the board for easing the game

    // We have to override the hash function given by the data class

) : Serializable {
    override fun hashCode(): Int {
        return getHash(x, y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SudokuNode

        if (x != other.x) return false
        if (y != other.y) return false
        if (value != other.value) return false
        if (readOnly != other.readOnly) return false

        return true
    }
}

internal fun getHash(x:Int , y: Int): Int {
    val newX = x*100
    return "$newX$y".toInt()
}
