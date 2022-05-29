package com.example.sudokump.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.viewmodel.SavedGamesScreenViewModel

@Composable
fun SavedGamesScreen() {

    val viewModel: SavedGamesScreenViewModel = viewModel()

    //val savedGames = rememberSaveable { viewModel.savedGames }
    val savedGames = viewModel.savedGames

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(savedGames.size)
        {
            SavedGameCard(savedGames[it])
        }
    }
}

@Composable
fun SavedGameCard(sudokuGameModel: SudokuGameModel)
{
    Card(modifier = Modifier.fillMaxWidth()) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

            val (id, timePassed, completionPercent, difficulty, saveDate) = createRefs()

            Text("ID: ${sudokuGameModel.id}", modifier = Modifier.constrainAs(id){
                top.linkTo(parent.top, 5.dp)
                start.linkTo(parent.start, 5.dp)
            })

            Text("Time Passed: ${sudokuGameModel.timePassed}", modifier = Modifier.constrainAs(timePassed){
                top.linkTo(id.bottom, margin = 5.dp)
                start.linkTo(id.start)
            })

            Text("Completion Percent: ${sudokuGameModel.completionPercent}", modifier = Modifier.constrainAs(completionPercent){
                top.linkTo(id.top)
                start.linkTo(id.end, margin = 5.dp)
            })

            Text("Difficulty: ${sudokuGameModel.difficulty}", modifier = Modifier.constrainAs(difficulty){
                top.linkTo(completionPercent.bottom, margin = 5.dp)
                start.linkTo(timePassed.end, margin = 5.dp)
            })

            Text("Saved: ${sudokuGameModel.saveDate.dayOfMonth}/${sudokuGameModel.saveDate.monthValue}/${sudokuGameModel.saveDate.year}",
                modifier = Modifier.constrainAs(saveDate){
                top.linkTo(id.top)
                start.linkTo(completionPercent.end, 5.dp)
            })
        }
    }
}