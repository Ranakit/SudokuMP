package com.example.sudokump.modules

import android.content.SharedPreferences
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.persistency.dao.SavedGamesDAO
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SudokuGamesEntryPoint {

    fun getSharedPreferences(): SharedPreferences
    fun getGamesDao(): SavedGamesDAO

    @EasySudokuGame
    fun getNewEasyGame() : SudokuGameModel

    @MediumSudokuGame
    fun getNewMediumGame(): SudokuGameModel

    @HardSudokuGame
    fun getNewHardGame(): SudokuGameModel
}