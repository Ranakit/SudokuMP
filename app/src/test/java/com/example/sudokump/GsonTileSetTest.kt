package com.example.sudokump

import com.example.sudokump.model.SudokuTileSet
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.junit.Assert.assertEquals
import org.junit.Test

class GsonTileSetTest {

    @Test
    fun testConversion()
    {
        val serialized = "{\"pairs\" : [[1,7],[2,3],[3,0],[3,1],[3,8],[4,0],[4,1],[5,2],[5,3],[5,5],[5,6],[5,7],[6,0],[6,1],[6,2],[6,4],[6,5],[6,7],[6,8],[7,0],[7,1],[7,2],[7,4],[7,8],[8,0],[8,3],[8,5],[8,7]]}"
        try {
            println(Gson().fromJson(serialized, SudokuTileSet::class.java).generateSet().toString())
            assertEquals(true, true)
        } catch (e: JsonSyntaxException)
        {
            println(e.message)
            assertEquals(true, false)
        }
    }
}