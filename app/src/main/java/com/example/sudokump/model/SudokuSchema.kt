package com.example.sudokump.model


class SudokuSchema{
    private val rows : MutableList<SudokuRow>
    private val columns : MutableList<SudokuColumn>
    private val matrices : MutableList<MutableList<SudokuSquare>>
    val map : HashMap<Int, SudokuNode>

    private constructor(lambda : (i : Int, j: Int) -> SudokuNode)
    {
        rows = arrayListOf()
        columns = arrayListOf()
        matrices = arrayListOf()
        map = hashMapOf()

        for(i in (0..8))
        {
            for(j in (0..8))
            {
                val sudokuNode = lambda(i,j)
                map[getHash(i,j)] = sudokuNode
            }
        }

        for (i in (0..8))
        {
            val rowList = arrayListOf<SudokuNode>()
            val colList = arrayListOf<SudokuNode>()

            for(j in 0..8)
            {
                rowList.add(map.getOrDefault(getHash(i,j), SudokuNode(0,0,0,false)))
                colList.add(map.getOrDefault(getHash(j,i), SudokuNode(0,0,0,false)))
            }

            rows.add(SudokuRow(i, rowList))
            columns.add(SudokuColumn(i, colList))
        }

        for (i in (0..2))
        {
            val matrixList = arrayListOf<SudokuSquare>()

            for(j in 0..2)
            {
                val matrix = hashMapOf<Int, SudokuNode>()

                for(l in 0..2) {

                    for(m in 0..2)
                    {
                        matrix[getHash(l,m)] = map.getOrDefault(getHash(3*i+l, 3*j+m), SudokuNode(0,0,0,false))
                    }
                }

                matrixList.add(SudokuSquare(i,j, matrix))

            }

            matrices.add(matrixList)
        }
    }

    constructor(sudokuGrid: SudokuGrid) : this({i,j -> SudokuNode(i,j,sudokuGrid.board[i][j], sudokuGrid.board[i][j]!=0)})

    constructor(sudokuGrid: SudokuGrid, sudokuTileSet: SudokuTileSet) : this({i, j -> SudokuNode(i,j,sudokuGrid.board[i][j], sudokuTileSet.generateSet().contains(
        Pair(i,j)
    ))})

    fun getReadOnlyTiles() : SudokuTileSet
    {
        val retVal = mutableListOf<MutableList<Int>>()
        for (i in 0..8)
        {
            for (j in 0..8)
            {
                if (map[getHash(i,j)]!!.readOnly)
                {
                    retVal.add(mutableListOf(i,j))
                }
            }
        }

        return SudokuTileSet(retVal)
    }

    fun overallCheck() : Boolean{
        return(checkAllRows() &&
        checkAllColumns() &&
        checkAllSquares())
    }

    private fun checkAllRows() : Boolean{
        for (elem in rows)
        {
            if(!elem.checkIsCorrect()){
                return false
            }
        }
        return true
    }

    private fun checkAllColumns() : Boolean{
        for (elem in columns)
        {
            if(!elem.checkIsCorrect()){
                return false
            }
        }
        return true
    }

    private fun checkAllSquares() : Boolean{
        for (elem in matrices)
        {
            for (elem1 in elem)
            if(!elem1.checkIsCorrect()){
                return false
            }
        }
        return true
    }


    fun overallCompletenessCheck() : Boolean
    {
        return checkAllRowsCompleteness() &&
                checkAllColumnsCompleteness() &&
                checkAllSquaresCompleteness()
    }

    private fun checkAllRowsCompleteness() : Boolean{
        for (elem in rows)
        {
            if(!elem.checkIsComplete()){
                return false
            }
        }
        return true
    }

    private fun checkAllColumnsCompleteness() : Boolean{
        for (elem in columns)
        {
            if(!elem.checkIsComplete()){
                return false
            }
        }
        return true
    }

    private fun checkAllSquaresCompleteness() : Boolean{
        for (elem in matrices)
        {
            for (elem1 in elem)
                if(!elem1.checkIsComplete()){
                    return false
                }
        }
        return true
    }


    fun evaluateCompletionPercent() : String
    {
        var counter = 0
        for(i in 0..8)
        {
            for(j in 0..8)
            {
                if(map[getHash(i,j)]?.value != 0)
                {
                    counter++
                }
            }
        }

        return String.format("%.2f%%", counter/81.0f*100)
    }

    fun getRowHints(position : Int) : Set<Pair<Int,SudokuNode>>
    {
        return rows[position].getHints()
    }

    fun getColumnHints(position : Int) : Set<Pair<Int,SudokuNode>>
    {
        return columns[position].getHints()
    }

    fun getSquareHints(xPos : Int, yPos : Int) : Set<Pair<Int,SudokuNode>>
    {
        return matrices[xPos][yPos].getHints()
    }

    fun copy() : SudokuSchema
    {
        return SudokuSchema(generateGrid())
    }

    private fun generateGrid() : SudokuGrid {
        val retVal = mutableListOf<MutableList<Int>>().apply {
            for (i in 0..8)
            {
                add(mutableListOf<Int>().apply { repeat(9) {add(0)} })
            }
        }

        for (elem in map.values)
        {
            retVal[elem.x][elem.y] = elem.value
        }

        return SudokuGrid(retVal)
    }
}