package com.fabrizio.sudoku.ui.activegame

import com.fabrizio.sudoku.domain.Difficulty
import com.fabrizio.sudoku.domain.SudokuPuzzle
import com.fabrizio.sudoku.domain.getHash


class ActiveGameViewModel {
    internal var subBoardState: ((HashMap<Int , SudokuTile>)-> Unit)? = null
    internal var subContentState: ((ActiveGameScreenState) -> Unit)? = null
    internal var subTimerSate : ((Long)-> Unit)?= null //the time needed by the user for complete a given sudoku



    internal fun updateTimerState(){
        timerState++
        subTimerSate?.invoke(1L)
    }
    internal var subIsCompleteState: ((Boolean) -> Unit)? = null
    internal var timerState: Long = 0L
    internal var difficulty = Difficulty.MEDIUM
    internal var boundary = 9
    internal var boardState : HashMap<Int, SudokuTile>
       = HashMap()
    internal var isCompleteState : Boolean = false
    internal var isNewRecordedState: Boolean = false

    fun initializeBoard(
        puzzle: SudokuPuzzle,
        isComplete : Boolean

    ) {
        puzzle.graph.forEach {
            val node = it.value[0]
            boardState[it.key] = SudokuTile(
                node.x, node.y, node.color, hasFocus = false, node.readOnly,
            )

        }
        val contentState: ActivegameScreen

        if (isComplete){
            isCompleteState = true
            contentState = ActiveGameContestState.COMPLETE
        } else{
            contentState = ActiveGameContestState.ACTIVE

        }
        boundary = puzzle.boundary
        difficulty = puzzle.difficulty
        timerState = puzzle.elapsedTime


        subIsCompleteState?.invoke(isCompleteState)
        subContentState?.invoke(contentState)
        subBoardState?.invoke(boardState)

    }


    internal fun updateBoardState(
        x: Int ,
        y: Int ,
        value : Int ,
        hasFocus: Boolean,
        readOnly: Boolean
    ){
        boardState[getHash(x,y)]?.let {
            it.value = value
            it.hasFocus = hasFocus
        }

        subBoardState?.invoke(boardState)
    }

    internal fun showLoadingState(){
        subContentState?.invoke(ActiveGameScreenState.LOADING)
    }

    internal fun updateFocusState( x: Int , y : Int ){
        boardState.values.forEach{
            it.hasFocus = it.x == x  &&  it.y == y
        }

        subBoardState?.invoke(boardState)

    }
    fun updateCompleteState(){
        isCompleteState = true
        subContentState?.invoke(ActiveGameScreenState.COMPLETE)
    }


}


class SudokuTile(
    val x : Int,
    val y : Int,
    var value : Int,
    var hasFocus : Boolean,   /*user has clicked on a tile , after he can change the number */
    val readOnly : Boolean    /*Can be a precompiled tile , in which the user can't change the number*/
)