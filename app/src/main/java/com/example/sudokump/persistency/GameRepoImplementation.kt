package com.example.sudokump.persistency

import android.content.Context
import android.content.SharedPreferences
import com.example.sudokump.domain.IGameRepository
import com.example.sudokump.domain.Settings
import com.example.sudokump.domain.SudokuNode
import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.modules.EasySudokuGame
import com.example.sudokump.modules.HardSudokuGame
import com.example.sudokump.modules.MediumSudokuGame
import com.example.sudokump.persistency.dao.SavedGamesDAO
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent


class GameRepoImplementation(private val context: Context): IGameRepository {

    private val sharedPreferences = EntryPointAccessors.fromApplication(context, GameRepoEntry::class.java).getSharedPreferences()


    override suspend fun createGame(difficulty: Difficulties, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val sudokuGame= when (difficulty) {
                Difficulties.EASY -> EntryPointAccessors.fromApplication(context, GameRepoEntry::class.java).getNewEasyGame()
                Difficulties.MEDIUM -> EntryPointAccessors.fromApplication(context, GameRepoEntry::class.java).getNewMediumGame()
                Difficulties.HARD -> EntryPointAccessors.fromApplication(context, GameRepoEntry::class.java).getNewHardGame() }
        sudokuGame.saveInDB(context)
        val newId = EntryPointAccessors.fromApplication(context, GameRepoEntry::class.java).getGamesDao().getNewGameId()
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        edit.putInt("id", newId)
        edit.commit()
        onSuccess.invoke()
    }

    override suspend fun saveGame(
            elapsedTime: Long,
            board: HashMap<Int, SudokuNode>,
            difficulty: Difficulties,
            onSuccess: (Unit)-> Unit,
            onError: (Exception) -> Unit,
    ) {
        val id = sharedPreferences.getInt("id", 0)
        SudokuGameModel(elapsedTime , id , board , difficulty).saveInDB(context)
        onSuccess.invoke(
            Unit
        )
    }

    override suspend fun updateGame(game: SudokuGameModel, onSuccess: (Unit) -> Unit, onError: (Exception) -> Unit) {

    }


    override suspend fun updateNode(
        x: Int,
        y: Int,
        color: Int,
        elapsedTime: Long,
        onSuccess: (isComplete: Boolean) -> Unit,
        onError: (Unit) -> Unit
    ) {


    }


    override suspend fun getCurrentGame(
        onSuccess: (SudokuGameModel, Boolean) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        val id = sharedPreferences.getInt("id" , -1)

        if (id == 0 )
            else if (id < 0 ) onError.invoke(Exception("wrong Id"))
        else {

            val DAO = EntryPointAccessors.fromApplication(context , GameRepoEntry::class.java).getGamesDao()
            onSuccess.invoke(SudokuGameModel(DAO.getLastSavedGame(id)), false)
        }

    }

    override suspend fun updateSettings(
        settings: Settings,
        onSuccess: (Unit) -> Unit,
        onError: (Exception) -> Unit
    ) {

    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GameRepoEntry{
        fun getSharedPreferences(): SharedPreferences
        fun getGamesDao(): SavedGamesDAO
        @EasySudokuGame
        fun getNewEasyGame() : SudokuGameModel
        @MediumSudokuGame
        fun getNewMediumGame(): SudokuGameModel
        @HardSudokuGame
        fun getNewHardGame(): SudokuGameModel
    }
}