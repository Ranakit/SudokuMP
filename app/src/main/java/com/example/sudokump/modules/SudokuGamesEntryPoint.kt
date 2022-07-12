package com.example.sudokump.modules

import com.example.sudokump.model.SudokuGameModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SudokuGamesEntryPoint {

    @EasySudokuGame
    fun getNewEasyGame() : SudokuGameModel

    @MediumSudokuGame
    fun getNewMediumGame(): SudokuGameModel

    @HardSudokuGame
    fun getNewHardGame(): SudokuGameModel
}