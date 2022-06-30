package com.example.sudokump.domain

import android.content.Context
import com.example.sudokump.model.Difficulties
import com.example.sudokump.ui.theme.activeGame.SudokuTile
import kotlinx.coroutines.Job


interface IGameRepository {

    suspend fun saveGame(
        elapsedTime: Long,
        id:Int,
        board: Map<Int , SudokuTile>,
        difficulty: Difficulties,
        onSuccess: (Unit)-> Unit,
        onError: (Exception) -> Unit,
        context: Context

    )


    suspend fun updateGame(   // Potrebbe essere cancellata
        //game: SudokuPuzzle,
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

        onSuccess: (Any, Any) -> Job,
        onError: (Exception )->Unit,
        context: Context

    )


    suspend fun updateSettings(
        settings: Settings,
        onSuccess: (Unit) -> Unit,
        onError: (Exception) -> Unit
    )
}