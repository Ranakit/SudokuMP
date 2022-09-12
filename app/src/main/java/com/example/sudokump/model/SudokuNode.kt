package com.example.sudokump.model

import java.io.Serializable

class SudokuNode(
    val x : Int,
    val y : Int,
    private var value: Int = 0,
    var readOnly: Boolean = true,
    val notes: MutableSet<Int> = mutableSetOf(),
    var isCorrect : Boolean = true
) : Serializable {

    lateinit var row: SudokuRow
    lateinit var column: SudokuColumn
    lateinit var square: SudokuSquare

    fun setValue(value : Int) {
        freeValue()
        if(value > 0) {
            if (getCrossAvailableSetInternal().contains(value)) {
                row.availableValues.remove(value)
                column.availableValues.remove(value)
                square.availableValues.remove(value)
            } else {
                this.isCorrect = false
            }

            this.value = value
        }
    }

    fun getValue() : Int {
        return this.value
    }

    fun getCrossAvailableSet() : Set<Int> {
        return if(value == 0) {row.availableValues.intersect(column.availableValues).intersect(square.availableValues)} else { setOf()}
    }

    private fun getCrossAvailableSetInternal() : Set<Int> {
        return row.availableValues.intersect(column.availableValues).intersect(square.availableValues)
    }

    private fun freeValue() {
        if(this.value != 0) {
            if(this.isCorrect) {
                row.propagateFreeValue(this.value)
                column.propagateFreeValue(this.value)
                square.propagateFreeValue(this.value)
            }
            this.value = 0
            this.isCorrect = true
        }
    }

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
