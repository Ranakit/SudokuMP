package com.example.sudokump.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.model.getHash
import com.example.sudokump.modules.ActiveGameViewModelAssistedFactory
import com.example.sudokump.modules.SudokuGamesEntryPoint
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext

class ActiveGameViewModel @AssistedInject constructor(
    @Assisted id : Int, @ApplicationContext context: Context) : ViewModel() {

    val game : SudokuGameModel
    private val entryPoint : SudokuGamesEntryPoint
    val boardState = HashMap<Int, SudokuTile>()

    val timerState : Long
    get() {
        return game.timePassed.inWholeSeconds
    }

    val isNewRecordedState = true

    val difficulty : Difficulties
    get()
    {
        return game.difficulty
    }

    companion object{
        fun provideFactory(
            assistedFactory: ActiveGameViewModelAssistedFactory,
            gameId : Int
        ) : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(gameId) as T
            }
        }
    }

    init {
        entryPoint = EntryPointAccessors.fromApplication(context, SudokuGamesEntryPoint::class.java)

        game = when{
            id == -2 -> entryPoint.getNewEasyGame()
            id == -1 -> entryPoint.getNewMediumGame()
            id == 0 -> entryPoint.getNewHardGame()
            id > 0 -> SudokuGameModel.getFromDB(context, id)
            else -> entryPoint.getNewEasyGame()
        }

        for(i in 0..9)
        {
            for(j in 0..9)
            {
                val node = game.schema.map[getHash(i,j)]
                boardState[getHash(i,j)] = SudokuTile(node!!.x, node.y, node.value, false, false)
            }
        }
    }

}