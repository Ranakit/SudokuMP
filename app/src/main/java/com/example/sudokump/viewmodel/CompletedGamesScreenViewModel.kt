package com.example.sudokump.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.modules.CompletedSudokuGames
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompletedGamesScreenViewModel @Inject constructor(@CompletedSudokuGames val completedGames : List<SudokuGameModel>) : ViewModel(){

    fun bestTime() : Int{
        var Index = 0
        for(item in completedGames){
            if (item.timePassed.compareTo(completedGames[Index].timePassed) < 0){
                Index = completedGames.indexOf(item)
            }
        }
        return Index
    }

    fun secondTime(bestTime: Int): Int {
        var bestIndex = when{
            bestTime == 0 -> 1
            else -> 0
        }
        for(item in completedGames){
            if (completedGames.indexOf(item) == bestTime){
                continue
            }
            else{
                if (item.timePassed.compareTo(completedGames[bestIndex].timePassed) < 0){
                    bestIndex = completedGames.indexOf(item)
                }
            }
        }
        return bestIndex
    }

    fun thirdTime(bestTime: Int, secondTime: Int): Int {
    var bestIndex = when{
            bestTime + secondTime == 1 -> 2
            bestTime == 0 -> 1
            secondTime == 0 -> 1
            else -> 0
        }
        for(item in completedGames){
            if (completedGames.indexOf(item) == bestTime || completedGames.indexOf(item) == secondTime){
                continue
            }
            else{
                if (item.timePassed.compareTo(completedGames[bestIndex].timePassed) < 0){
                    bestIndex = completedGames.indexOf(item)
                }
            }
        }
        return bestIndex
    }
}