package com.example.sudokump.model

import com.example.sudokump.common.AsyncReturnTask

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
            if (node.value == 0) {
                val taskList = mutableListOf<Pair<Int,SudokuSolver>>()

                for (num in node.getCrossAvailableSet()) {
                    val copy = schema.copy()
                    copy.map[getHash(node.x, node.y)]?.setValueWithNotification(num)
                    val solver = SudokuSolver(copy)
                    solver.executeAsyncReturnTask()
                    taskList.add(Pair(num, solver))
                }

                taskList.forEach {
                    it.second.joinTask().forEach { solvedSchema ->
                        if(solvedSchema.overallCheck() && solvedSchema.overallCompletenessCheck()) {
                            return Pair(it.first, node)
                        }
                    }

                }
            }
        }

        return Pair(null, null)
    }
}