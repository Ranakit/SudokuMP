package com.example.sudokump.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.modules.SavedSudokuGames
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedGamesScreenViewModel @Inject constructor(@SavedSudokuGames val savedGames: List<SudokuGameModel>) : ViewModel()
{
    val selectedGame = mutableStateOf(-1)
}