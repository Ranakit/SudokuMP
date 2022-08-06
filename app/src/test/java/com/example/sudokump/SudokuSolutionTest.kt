package com.example.sudokump

import com.example.sudokump.model.*
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class SudokuSolutionTest {

    @Test
    fun testSudokuSolution()
    {
        val savedSchema = "{\"board\":[[0,0,4,8,7,0,0,0,0],[0,2,0,0,0,0,7,8,0],[0,0,0,0,0,9,0,4,0],[0,1,0,0,0,0,0,0,0],[0,4,0,1,0,0,0,2,0],[7,8,9,0,6,0,0,1,4],[0,3,0,6,8,0,0,7,0],[0,5,0,9,0,4,0,0,1],[8,0,0,7,1,3,4,0,5]]}"
        val schema = SudokuSchema(Gson().fromJson(savedSchema, SudokuGrid::class.java))
        val copy = schema.copy()

        val solver = SudokuSolver(copy)
        solver.executeAsyncReturnTask()
        val solutions = solver.joinTask()

        var test = true

        for (solution in solutions) {
            prettyPrintSudoku(solution)
            test = test && (solution.overallCheck() && solution.overallCompletenessCheck())
        }

        assertEquals(true, test)
    }

    private fun prettyPrintSudoku(schema: SudokuSchema) {
        var string = ""

        for (i in 0..8) {
            for (j in 0..8) {
                string += "${schema.map[getHash(i,j)]!!.value} "
            }

            string += "\n"
        }

        println(string)
    }
}