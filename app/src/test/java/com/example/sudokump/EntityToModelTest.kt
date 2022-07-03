package com.example.sudokump

import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.model.SudokuGrid
import com.example.sudokump.persistency.entities.SavedGameDBEntity
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test


class EntityToModelTest {
    @Test
    fun json_conversion_working()
    {
        val savedSchema = "{\"board\":[[0,0,4,8,7,0,0,0,0],[0,2,0,0,0,0,7,8,0],[0,0,0,0,0,9,0,4,0],[0,1,0,0,0,0,0,0,0],[0,4,0,1,0,0,0,2,0],[7,8,9,0,6,0,0,1,4],[0,3,0,6,8,0,0,7,0],[0,5,0,9,0,4,0,0,1],[8,0,0,7,1,3,4,0,5]]}"
        val savedSchemaByteArray = ByteArray(savedSchema.length +1)
        for (i in savedSchema.indices)
        {
            savedSchemaByteArray[i] = savedSchema[i].code.toByte()
        }
        val entity = SavedGameDBEntity(1, 40, "4%", savedSchemaByteArray, "EASY", "27/05/2022")
        val model = SudokuGameModel(entity)

        println(model.id)
        println(model.completionPercent)
        println(model.difficulty)
        println(model.saveDate)
        println(model.timePassed)
        println(model.schema)

        val gson = Gson()

        val board = gson.fromJson(savedSchema, SudokuGrid::class.java)

        assertEquals(board.board, model.schema)
    }
}