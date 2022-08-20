package com.example.sudokump.model

import com.example.sudokump.common.MutablePair

abstract class SudokuComposition
{
    protected fun checkList(sorted : List<SudokuNode>) : Boolean{
        var lastNum = 0

        for(elem in sorted)
        {
            if(elem.getValue() != 0)
            {
                if(elem.getValue() != lastNum)
                {
                    lastNum = elem.getValue()
                }
                else {
                    return false
                }
            }
        }

        return true
    }

    protected fun checkListCompleteness(sorted: List<SudokuNode>) : Boolean
    {

        for ((lastChecked, elem) in sorted.withIndex())
        {
            if (elem.getValue() != lastChecked +1)
            {
                return false
            }

        }

        return true
    }

    abstract fun checkIsCorrect() : Boolean
    abstract fun getHints() : Set<Pair<Int,SudokuNode>>
    protected abstract fun hasOnlyThatPossibleValue() : MutableSet<Pair<Int, SudokuNode>>
    protected abstract fun isTheOnlyPossibleValue() : MutableSet<Pair<Int, SudokuNode>>
    abstract fun checkIsComplete() : Boolean
}

abstract class LinearSudokuComposition(private val nodes : List<SudokuNode>) : SudokuComposition()
{
    override fun checkIsCorrect() : Boolean
    {
        val toSort = mutableListOf<SudokuNode>()
        toSort.addAll(nodes)

        toSort.sortWith(comparator)

        return checkList(toSort)
    }

    override fun checkIsComplete(): Boolean {
        val toSort = mutableListOf<SudokuNode>()
        toSort.addAll(nodes)

        toSort.sortWith(comparator)

        return checkListCompleteness(toSort)
    }

    override fun getHints() : Set<Pair<Int, SudokuNode>>
    {
        return hasOnlyThatPossibleValue().union(isTheOnlyPossibleValue())
    }

    override fun hasOnlyThatPossibleValue() : MutableSet<Pair<Int, SudokuNode>>
    {
        val retVal = mutableSetOf<Pair<Int, SudokuNode>>()

        for (node in nodes)
        {
            val crossAvailableSet = node.getCrossAvailableSet()
            if (crossAvailableSet.size == 1)
            {
                retVal.add(Pair(crossAvailableSet.first(),node))
            }
        }

        return retVal
    }

    override fun isTheOnlyPossibleValue() : MutableSet<Pair<Int, SudokuNode>> {
        val occurrenciesList = mutableListOf<MutablePair<Int, SudokuNode?>>().apply {
            repeat(9) {
                add(MutablePair(0, null))
            }
        }

        for (node in nodes) {
            for (num in node.getCrossAvailableSet()) {
                val pair = occurrenciesList[num - 1]
                if (pair.first == 0) {
                    pair.first = 1
                    pair.second = node
                } else if (pair.first == 1) {
                    pair.first = 2
                    pair.second = null
                }
            }
        }

        val retVal = mutableSetOf<Pair<Int, SudokuNode>>()
        for (i in (0 until occurrenciesList.size)) {
            if (occurrenciesList[i].first == 1) {
                retVal.add(Pair(i+1, occurrenciesList[i].second!!))
            }
        }

        return retVal
    }
}

class SudokuRow(val position : Int, private val nodes : List<SudokuNode>) : LinearSudokuComposition(nodes)
{
    val availableValues = mutableSetOf(1,2,3,4,5,6,7,8,9)

    init {
        for (node in nodes)
        {
            node.row = this
            availableValues.remove(node.getValue())
        }
    }
}

class SudokuColumn(val position : Int, private val nodes : List<SudokuNode>) : LinearSudokuComposition(nodes)
{
    val availableValues = mutableSetOf(1,2,3,4,5,6,7,8,9)

    init {
        for (node in nodes)
        {
            node.column = this
            availableValues.remove(node.getValue())
        }
    }
}

class SudokuSquare(val xPos : Int, val yPos : Int, private val matrix : HashMap<Int, SudokuNode>) : SudokuComposition()
{
    val availableValues = mutableSetOf(1,2,3,4,5,6,7,8,9)

    init {
        for (node in matrix.values)
        {
            node.square = this
            availableValues.remove(node.getValue())
        }
    }

    override fun checkIsCorrect() : Boolean
    {
        val toSort = mutableListOf<SudokuNode>()
        toSort.addAll(matrix.values)

        toSort.sortWith(comparator)

        return checkList(toSort)
    }

    override fun checkIsComplete(): Boolean {
        val toSort = mutableListOf<SudokuNode>()
        toSort.addAll(matrix.values)

        toSort.sortWith(comparator)

        return checkListCompleteness(toSort)
    }

    override fun getHints(): Set<Pair<Int, SudokuNode>> {
        return hasOnlyThatPossibleValue().union(isTheOnlyPossibleValue())
    }

    override fun hasOnlyThatPossibleValue(): MutableSet<Pair<Int, SudokuNode>> {
        val retVal = mutableSetOf<Pair<Int, SudokuNode>>()

        for (node in matrix.values)
        {
            val crossAvailableSet = node.getCrossAvailableSet()
            if (crossAvailableSet.size == 1)
            {
                retVal.add(Pair(crossAvailableSet.first(),node))
            }
        }

        return retVal
    }

    override fun isTheOnlyPossibleValue(): MutableSet<Pair<Int, SudokuNode>> {
        val occurrenciesList = mutableListOf<MutablePair<Int, SudokuNode?>>().apply {
            repeat(9) {
                add(MutablePair(0, null))
            }
        }

        for (node in matrix.values) {
            for (num in node.getCrossAvailableSet()) {
                val pair = occurrenciesList[num - 1]
                if (pair.first == 0) {
                    pair.first = 1
                    pair.second = node
                } else if (pair.first == 1) {
                    pair.first = 2
                    pair.second = null
                }
            }
        }

        val retVal = mutableSetOf<Pair<Int, SudokuNode>>()
        for (i in (0 until occurrenciesList.size)) {
            if (occurrenciesList[i].first == 1) {
                retVal.add(Pair(i+1, occurrenciesList[i].second!!))
            }
        }

        return retVal
    }
}

private val comparator = Comparator{node1 : SudokuNode, node2 : SudokuNode ->
    node1.getValue() - node2.getValue()
}