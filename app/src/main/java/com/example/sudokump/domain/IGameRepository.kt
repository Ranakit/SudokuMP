package com.example.sudokump.domain

import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.model.SudokuNode


interface IGameRepository {

    suspend fun createGame(
        difficulty: Difficulties,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun saveGame(
        elapsedTime: Long,
        board: HashMap<Int , SudokuNode>,
        difficulty: Difficulties,
        onSuccess: (Unit)-> Unit,
        onError: (Exception) -> Unit,

        )

    suspend fun updateGame(   // Potrebbe essere cancellata
        game: SudokuGameModel,
        onSuccess: (Unit) -> Unit,
        onError: (Exception ) -> Unit
    )

    suspend fun updateNode(     // Potrebbe essere cancellata
        x: Int,
        y: Int,
        color: Int,
        elapsedTime: Long,
        onSuccess: (isComplete: Boolean) -> Unit,
        onError: (Unit) -> Unit
    )
    /*
    When a user return to an active game , we want to get a current game.
    The edge case is when a user complete a game and leave , and in this case
    if he return to the game we continue to pass the flag isComplete

     */
    suspend fun getCurrentGame(

        onSuccess: (SudokuGameModel, Boolean) -> Unit,
        onError: (Exception ) -> Unit,

    )
}