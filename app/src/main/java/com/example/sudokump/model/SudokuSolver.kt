package com.example.sudokump.model

import com.example.sudokump.common.AsyncReturnTask

data class SudokuSolver(private val schema: SudokuSchema) : AsyncReturnTask<List<SudokuSchema>>() {

    override fun taskToExecuteWithReturn(): List<SudokuSchema> {

        val schemas = mutableListOf<SudokuSchema>().apply { add(schema) }

        mainLoop@ for(schema in schemas) {

            schemaLoop@ while (!schema.overallCompletenessCheck()) {

                for (i in 0..8) {
                    val hints = schema.getRowHints(i)
                    if (hints.isNotEmpty()) {
                        for (hint in hints) {
                            hint.second.setValueWithNotification(hint.first)
                        }
                        continue@schemaLoop
                    }
                }

                for (i in 0..8) {
                    val hints = schema.getColumnHints(i)
                    if (hints.isNotEmpty()) {
                        for (hint in hints) {
                            hint.second.setValueWithNotification(hint.first)
                        }
                        continue@schemaLoop
                    }
                }

                for (i in 0..2) {
                    for (j in 0..2) {
                        val hints = schema.getSquareHints(i, j)
                        if (hints.isNotEmpty()) {
                            for (hint in hints) {
                                hint.second.setValueWithNotification(hint.first)
                            }
                            continue@schemaLoop
                        }
                    }
                }

                for (node in schema.map.values) {
                    if (node.value == 0) {
                        val taskList = mutableListOf<SudokuSolver>()

                        for (num in node.getCrossAvailableSet()) {
                            val copy = schema.copy()
                            copy.map[getHash(node.x, node.y)]?.setValueWithNotification(num)
                            val solver = SudokuSolver(copy)
                            solver.executeAsyncReturnTask()
                            taskList.add(solver)
                        }

                        schemas.remove(schema)

                        taskList.forEach {
                            schemas.addAll(it.joinTask())
                        }


                        break@mainLoop
                    }
                }
            }
        }

        return mutableListOf<SudokuSchema>().apply {
            schemas.forEach {
                if (it.overallCompletenessCheck()) {
                    add(it)
                }
            }
        }
    }
}