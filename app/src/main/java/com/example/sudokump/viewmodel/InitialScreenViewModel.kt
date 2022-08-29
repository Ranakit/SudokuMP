package com.example.sudokump.viewmodel

import android.content.Context
import com.example.sudokump.persistency.dao.SavedGamesDAO
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class InitialScreenViewModel@AssistedInject constructor(
    @ApplicationContext context: Context
) {
    val context: Context

    init {
        this.context = context
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface InitialScreenEntryPoint {
        fun getSavedGamesDAO() : SavedGamesDAO
    }

    val dao = EntryPointAccessors.fromApplication(context, InitialScreenEntryPoint::class.java).getSavedGamesDAO()
    val isSavedGames = isSavedGames(dao)
    val isCompletedGames = isCompletedGames(dao)

    private fun isSavedGames(dao: SavedGamesDAO): Boolean {
        var isSavedGames = false
        val job = CoroutineScope(Dispatchers.IO).launch {
            isSavedGames = dao.getSavedGames().isNotEmpty()
        }
        runBlocking {
            job.join()
        }
        return isSavedGames
    }

    private fun isCompletedGames(dao: SavedGamesDAO): Boolean {
        var isCompletedGames = false
        val job = CoroutineScope(Dispatchers.IO).launch {
            isCompletedGames = dao.getCompletedGames().isNotEmpty()
        }
        runBlocking {
            job.join()
        }
        return isCompletedGames
    }
}