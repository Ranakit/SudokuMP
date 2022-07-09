package com.example.sudokump.model

data class SudokuTileSet(val pairs : List<List<Int>>) {

    fun generateSet() : HashSet<Pair<Int,Int>>
    {
        val retVal = hashSetOf<Pair<Int,Int>>()

        for (elem in pairs)
        {
            retVal.add(Pair(elem[0],elem[1]))
        }

        return retVal
    }
}