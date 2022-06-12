package com.fabrizio.sudoku.domain

interface IGameDataStorage {

    suspend fun updateGame(game: SudokuPuzzle): GameStorageResult
    suspend fun updateNode(x: Int , y: Int , elapsedTime: Long): GameStorageResult
    suspend fun getCurrentGame(): GameStorageResult
}




/*
this class allows to return an object from a particular function contained in the upper interface , and this
object can represents different multiple states.
 */
sealed class GameStorageResult{

    data class OnSuccess(val currentgame: SudokuPuzzle): GameStorageResult()
    data class OnErrore(val exception: Exception): GameStorageResult()


}