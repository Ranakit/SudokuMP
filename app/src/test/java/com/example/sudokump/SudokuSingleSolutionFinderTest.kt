package com.example.sudokump

import com.example.sudokump.model.*
import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test

class SudokuSingleSolutionFinderTest {

    @Test
    fun testSudokuSolution()
    {
        //Easy
        //val savedSchema = "{\"board\":[[0,0,4,8,7,0,0,0,0],[0,2,0,0,0,0,7,8,0],[0,0,0,0,0,9,0,4,0],[0,1,0,0,0,0,0,0,0],[0,4,0,1,0,0,0,2,0],[7,8,9,0,6,0,0,1,4],[0,3,0,6,8,0,0,7,0],[0,5,0,9,0,4,0,0,1],[8,0,0,7,1,3,4,0,5]]}"
        //Hard
        val savedSchema = "{\"board\":[[0,0,0,0,0,0,0,0,0],[2,0,0,0,0,0,0,0,0],[7,0,0,0,0,0,0,4,0],[0,0,3,0,6,0,0,0,0],[4,0,0,0,9,8,2,0,6],[0,0,8,0,0,2,4,0,3],[3,4,1,0,0,0,0,0,7],[8,0,5,6,0,3,0,0,0],[9,0,0,0,0,1,0,0,0]]}"
        //Medium
        //val savedSchema = "{\"board\":[[0,0,0,0,2,0,0,0,0],[0,0,0,0,5,6,0,0,0],[0,8,0,0,0,0,0,0,0],[2,0,4,3,0,0,0,0,0],[3,5,0,7,8,9,0,1,0],[7,9,0,0,1,0,5,0,3],[5,3,0,0,4,2,0,0,8],[0,0,7,5,9,3,6,2,0],[0,6,0,0,7,0,0,0,0]]}"

        //Medium
        //val savedSchema = "{\"board\":[[6,0,8,0,0,0,0,7,0],[7,4,0,0,0,9,0,0,0],[0,0,0,0,4,0,0,3,8],[5,0,0,4,0,2,0,0,0],[0,0,4,9,5,0,0,8,0],[0,7,0,0,3,0,0,0,0],[0,3,0,0,0,0,0,9,0],[0,0,5,3,0,0,0,0,6],[9,6,0,8,1,0,0,0,5]]}"

        val schema = SudokuSchema(Gson().fromJson(savedSchema, SudokuGrid::class.java))
        val copy = schema.copy()

        val finder = SudokuSingleSolutionFinder(copy)
        finder.executeAsyncReturnTask()
        val solution = finder.joinTask()

        var test = false

        if(solution != null) {
            prettyPrintSudoku(solution)
            test = test || (solution.overallCheck() && solution.overallCompletenessCheck())
        }

        Assert.assertEquals(true, test)
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