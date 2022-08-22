package com.example.sudokump.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.modules.SavedSudokuGames
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SavedGamesScreenViewModel @Inject constructor(@SavedSudokuGames val savedGames: List<SudokuGameModel>, @ApplicationContext context: Context) : ViewModel()
{
    val context : Context
    val selectedGame = mutableStateOf(-1)

    init {
        this.context = context
    }
}