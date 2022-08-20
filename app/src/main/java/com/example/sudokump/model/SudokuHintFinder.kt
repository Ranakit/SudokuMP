package com.example.sudokump.model

import android.util.Log
import com.example.sudokump.common.AsyncReturnTask
import com.example.sudokump.common.NoReturnException

class SudokuHintFinder(private val schema: SudokuSchema) : AsyncReturnTask<Pair<Int?, SudokuNode?>>() {

    override fun taskToExecuteWithReturn(): Pair<Int?, SudokuNode?> {

        for (i in 0..8) {
            val hints = schema.getRowHints(i)
            if (hints.isNotEmpty()) {
                return hints.first()
            }
        }

        for (i in 0..8) {
            val hints = schema.getColumnHints(i)
            if (hints.isNotEmpty()) {
                return hints.first()
            }
        }

        for (i in 0..2) {
            for (j in 0..2) {
                val hints = schema.getSquareHints(i, j)
                if (hints.isNotEmpty()) {
                    return hints.first()
                }
            }
        }

        for (node in schema.map.values) {
            if (node.getValue() == 0) {

                for (num in node.getCrossAvailableSet()) {
                    val copy = schema.copy()
                    copy.map[getHash(node.x, node.y)]?.setValue(num)
                    val finder = SudokuSingleSolutionFinder(copy)
                    finder.executeAsyncReturnTask()
                    var solution : SudokuSchema? = null

                    try {
                        solution = finder.joinTask()
                    }catch (e : NoReturnException) {
                        Log.i("SudokuMP", "No solution found")
                        println("No solution found")
                    }

                    if(solution != null && solution.overallCompletenessCheck() && solution.overallCheck()) {
                        return Pair(num, node)
                    }
                }

                /*taskList.forEach {
                    it.second.joinTask().forEach { solvedSchema ->
                        if(solvedSchema.overallCheck() && solvedSchema.overallCompletenessCheck()) {
                            return Pair(it.first, node)
                        }*/
            }
        }

        return Pair(null, null)
    }
}