package com.example.sudokump.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sudokump.model.SudokuGameModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedGamesScreenViewModel @Inject constructor(val savedGames: List<SudokuGameModel>) : ViewModel()
{
}