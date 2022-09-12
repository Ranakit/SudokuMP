package com.example.sudokump.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.modules.CompletedSudokuGames
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompletedGamesScreenViewModel @Inject constructor(@CompletedSudokuGames val completedGames : List<SudokuGameModel>) : ViewModel()

