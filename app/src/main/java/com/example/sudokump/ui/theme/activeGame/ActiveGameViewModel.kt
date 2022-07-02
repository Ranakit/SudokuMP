package com.example.sudokump.ui.theme.activeGame

import androidx.lifecycle.ViewModel
import com.example.sudokump.domain.SudokuNode
import com.example.sudokump.domain.SudokuPuzzle
import com.example.sudokump.domain.getHash
import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel

class ActiveGameViewModel(): ViewModel() {
    internal var onStartBehavior: (() -> Unit)? = null
    internal var onStopBehavior: (() -> Unit)? = null
    internal var subBoardState: ((HashMap<Int , SudokuTile>)-> Unit)? = null
    internal var subContentState: ((ActiveGameScreenState) -> Unit)? = null
    internal var subTimerState : ((Long)-> Unit)?= null //the time needed by the user for complete a given sudoku

    fun onStart() {
        if (onStartBehavior == null){}
        else{
            onStartBehavior!!.invoke()
        }
    }

    fun onStop(){
        if (onStopBehavior == null){}
        else{
            onStopBehavior!!.invoke()
        }
    }

    internal fun updateTimerState(){
        timerState++
        subTimerState?.invoke(1L)
    }
    internal var subIsCompleteState: ((Boolean) -> Unit)? = null
    internal var timerState: Long = 0L
    internal var difficulty: Difficulties = Difficulties.MEDIUM
    internal var boundary = 9
    internal var boardState : HashMap<Int, SudokuTile> = HashMap()
    internal var isCompleteState : Boolean = false
    internal var isNewRecordedState: Boolean = false

    fun initializeBoard(puzzle: SudokuPuzzle, isComplete : Boolean) {
        puzzle.graph.forEach {
            val node = it.value[0]
            boardState[it.key] = SudokuTile(
                node.x,
                node.y,
                node.color,
                false,
                node.readOnly
            )
        }
        val contentState: ActiveGameScreenState

        if (isComplete){
            isCompleteState = true
            contentState = ActiveGameScreenState.COMPLETE
        } else{
            contentState = ActiveGameScreenState.ACTIVE

        }
        boundary = puzzle.boundary
        difficulty = puzzle.difficulty
        timerState = puzzle.elapsedTime


        subIsCompleteState?.invoke(isCompleteState)
        subContentState?.invoke(contentState)
        subBoardState?.invoke(boardState)

    }

    fun initializeBoardState(puzzle: SudokuGameModel, isComplete: Boolean) {
        puzzle.schema.forEach {
            val node = it.value
            boardState[it.key] = SudokuTile(
                node.x,
                node.y,
                node.color,
                false,
                node.readOnly
            )
        }
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
    val readOnly : Boolean    /*Can be a precompiled tile , in which the user can't change the number*/)
