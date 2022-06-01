package com.example.sudokump.modules

import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.persistency.db.SudokuDB
import com.example.sudokump.persistency.entities.SavedGameDBEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Module
@InstallIn(ViewModelComponent::class)
object SavedSudokuGamesProvider {

    @Provides
    fun provideSavedSudokuGames(db : SudokuDB) : List<SudokuGameModel> {

        var savedGamesDBEntities : List<SavedGameDBEntity> = listOf()
        val savedGames : MutableList<SudokuGameModel> = mutableListOf()

        runBlocking {
            val recoverJob = CoroutineScope(Dispatchers.IO).launch {
                val gamesDAO = db.getSavedGamesDAO()
                savedGamesDBEntities = gamesDAO.getSavedGames()
            }

            recoverJob.join()
        }

        for (savedGame in savedGamesDBEntities)
        {
            savedGames.add(SudokuGameModel(savedGame))
        }

        return savedGames
    }
}