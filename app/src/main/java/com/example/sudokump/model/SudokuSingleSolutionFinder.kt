package com.example.sudokump.model

import com.example.sudokump.common.AsyncReturnTask
import com.example.sudokump.common.NoReturnException

class SudokuSingleSolutionFinder(private val schema: SudokuSchema) : AsyncReturnTask<SudokuSchema?>() {

    override fun taskToExecuteWithReturn(): SudokuSchema? {

        var retVal : SudokuSchema? = schema

        schemaLoop@ while (retVal != null && !retVal.overallCompletenessCheck()) {

            for (i in 0..8) {
                val hints = retVal.getRowHints(i)
                if (hints.isNotEmpty()) {
                    for (hint in hints) {
                        hint.second.setValue(hint.first)
                    }
                    continue@schemaLoop
                }
            }

            for (i in 0..8) {
                val hints = retVal.getColumnHints(i)
                if (hints.isNotEmpty()) {
                    for (hint in hints) {
                        hint.second.setValue(hint.first)
                    }
                    continue@schemaLoop
                }
            }

            for (i in 0..2) {
                for (j in 0..2) {
                    val hints = retVal.getSquareHints(i, j)
                    if (hints.isNotEmpty()) {
                        for (hint in hints) {
                            hint.second.setValue(hint.first)
                        }
                        continue@schemaLoop
                    }
                }
            }

            for (node in retVal!!.map.values) {
                if (node.getValue() == 0) {

                    for (num in node.getCrossAvailableSet()) {
                        val copy = retVal!!.copy()
                        copy.map[getHash(node.x, node.y)]?.setValue(num)

                        val finder = SudokuSingleSolutionFinder(copy)
                        finder.executeAsyncReturnTask()
                        var trySchema: SudokuSchema? = null
                        try {
                            trySchema = finder.joinTask()
                        }catch (e : NoReturnException)
                        {
                            println("No solution found")
                        }

                        if(trySchema!= null && trySchema.overallCheck() && trySchema.overallCompletenessCheck()) {
                            retVal = trySchema
                            break@schemaLoop
                        }
                    }

                    retVal = null
                    break@schemaLoop
                }
            }
        }

        return retVal
    }
}