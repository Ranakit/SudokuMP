package com.example.sudokump.model


class SudokuSchema(sudokuGrid: SudokuGrid) {
    val rows : MutableList<SudokuRow>
    val columns : MutableList<SudokuColumn>
    val matrices : MutableList<MutableList<SudokuSquare>>
    val map : HashMap<Int, SudokuNode>

    init {
        rows = arrayListOf()
        columns = arrayListOf()
        matrices = arrayListOf()
        map = hashMapOf()

        for(i in (0..9))
        {
            for(j in (0..9))
            {
                val sudokuNode = SudokuNode(i,j,sudokuGrid.board[i][j], false)
                map[getHash(i,j)] = sudokuNode
            }
        }

        for (i in (0..9))
        {
            val rowList = arrayListOf<SudokuNode>()
            val colList = arrayListOf<SudokuNode>()

            for(j in 0..9)
            {
                rowList.add(map.getOrDefault(getHash(i,j), SudokuNode(0,0,0,false)))
                colList.add(map.getOrDefault(getHash(j,i), SudokuNode(0,0,0,false)))
            }

            rows.add(SudokuRow(i, rowList))
            columns.add(SudokuColumn(i, colList))
        }

        for (i in (0..3))
        {
            val matrixList = arrayListOf<SudokuSquare>()

            for(j in 0..3)
            {
                val matrix = hashMapOf<Int, SudokuNode>()

                for(l in 0..3) {

                    for(m in 0..3)
                    {
                        matrix[getHash(l,m)] = map.getOrDefault(getHash(i+l, j+m), SudokuNode(0,0,0,false))
                    }
                }

                matrixList.add(SudokuSquare(i,j, matrix))

            }

            matrices.add(matrixList)
        }
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
}

abstract class SudokuComposition()
{
    protected fun checkList(sorted : List<SudokuNode>) : Boolean{
        var lastNum = 0

        for(elem in sorted)
        {
            if(elem.value != 0)
            {
                if(elem.value != lastNum)
                {
                    lastNum = elem.value
                }
                else {
                    return false
                }
            }
        }

        return true
    }
}

class SudokuRow(val position : Int, val nodes : List<SudokuNode>) : SudokuComposition()
{
    fun checkIsCorrect() : Boolean
    {
        val toSort = mutableListOf<SudokuNode>()
        toSort.addAll(nodes)

        toSort.sortWith(comparator)

        return checkList(toSort)
    }
}

class SudokuColumn(val position : Int, val nodes : List<SudokuNode>) : SudokuComposition()
{
    fun checkIsCorrect() : Boolean
    {
        val toSort = mutableListOf<SudokuNode>()
        toSort.addAll(nodes)

        toSort.sortWith(comparator)

        return checkList(toSort)
    }
}

class SudokuSquare(val xPos : Int, val yPos : Int, val matrix : HashMap<Int, SudokuNode>) : SudokuComposition()
{
    fun checkIsCorrect() : Boolean
    {
        val toSort = mutableListOf<SudokuNode>()
        toSort.addAll(matrix.values)

        toSort.sortWith(comparator)

        return checkList(toSort)
    }
}

private val comparator = Comparator{node1 : SudokuNode, node2 : SudokuNode ->
    node1.value - node2.value
}
