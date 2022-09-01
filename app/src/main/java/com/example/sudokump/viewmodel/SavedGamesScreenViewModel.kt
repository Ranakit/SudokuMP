package com.example.sudokump.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.modules.SavedSudokuGames
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SavedGamesScreenViewModel @Inject constructor(@SavedSudokuGames val savedGames: List<SudokuGameModel>, @ApplicationContext context: Context, val sharedPreferences: SharedPreferences) : ViewModel()
{
    val context : Context
    val selectedGame = mutableStateOf(-1)

    init {
        this.context = context
    }

    fun onDeleteClick(gameId : Int) {
        if(sharedPreferences.getInt("id", -1) == gameId) {
            val editor = sharedPreferences.edit()

            editor.putInt("id", -1)

            editor.apply()
        }
    }
}