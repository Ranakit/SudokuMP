package com.example.sudokump.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.sudokump.R
import com.example.sudokump.common.mapToList
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.viewmodel.SavedGamesScreenViewModel

@Composable
fun SavedGamesScreen(viewModel: SavedGamesScreenViewModel, navController: NavController) {

    val savedGames = viewModel.savedGames

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .clickable {
            viewModel.selectedGame.value = -1
        }) {
        items(savedGames.size)
        {
            SavedGameCard(viewModel, savedGames[it], it, viewModel.selectedGame, navController)
        }
    }
}

@Composable
fun SavedGameCard(viewModel: SavedGamesScreenViewModel, sudokuGameModel: SudokuGameModel, position: Int, mutableFlag: MutableState<Int>, navController: NavController)
{
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            mutableFlag.value = position
        }) {
        Column(modifier = Modifier.fillMaxWidth()) {

            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

                val (grid, id, timePassed, completionPercent, difficulty, saveDate) = createRefs()

                CompletionGridCanvas(schema = mapToList(sudokuGameModel.schema.map), modifier = Modifier
                    .size(50.dp)
                    .constrainAs(grid) {
                        top.linkTo(parent.top, 5.dp)
                        start.linkTo(parent.start, 5.dp)
                        bottom.linkTo(parent.bottom, 5.dp)
                    })

                Text(
                    "${stringResource(id = R.string.id)}: ${sudokuGameModel.id}",
                    modifier = Modifier.constrainAs(id){
                    top.linkTo(grid.top, 0.dp)
                    start.linkTo(grid.end, 5.dp)
                    },
                    //style =
                )

                Text("${stringResource(id = R.string.timePassed)}: ${sudokuGameModel.timePassed}", modifier = Modifier.constrainAs(timePassed){
                    top.linkTo(id.bottom, margin = 5.dp)
                    start.linkTo(id.start)
                })

                Text("${stringResource(id = R.string.completionPercent)}: ${sudokuGameModel.completionPercent}", modifier = Modifier.constrainAs(completionPercent){
                    top.linkTo(id.top)
                    start.linkTo(id.end, margin = 5.dp)
                })

                Text("${stringResource(id = R.string.difficulty)}: ${sudokuGameModel.difficulty}", modifier = Modifier.constrainAs(difficulty){
                    top.linkTo(completionPercent.bottom, margin = 5.dp)
                    start.linkTo(timePassed.end, margin = 5.dp)
                })

                Text("${stringResource(id = R.string.savedWhen)}: ${sudokuGameModel.saveDate.dayOfMonth}/${sudokuGameModel.saveDate.monthValue}/${sudokuGameModel.saveDate.year}",
                    modifier = Modifier.constrainAs(saveDate){
                        top.linkTo(id.top)
                        start.linkTo(completionPercent.end, 5.dp)
                    })
            }

            if (mutableFlag.value == position)
            {
                Divider(thickness = 1.dp, color = Color.LightGray)

                OptionButtons(viewModel, navController, sudokuGameModel)
            }
        }
    }
}

@Composable
fun OptionButtons(viewModel: SavedGamesScreenViewModel, navController: NavController, sudokuGameModel: SudokuGameModel)
{
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(onClick = {sudokuGameModel.deleteFromDB(viewModel.context)
            navController.backQueue.clear()
            navController.navigate("Init")
            navController.navigate("SavedGames")
        }) {
            Text(text = stringResource(id = R.string.delete_game))
        }

        Button(onClick = { navController.navigate("game/${sudokuGameModel.id}") }) {
            Text(text = stringResource(id = R.string.continue_game))
        }
    }
}

@Composable
fun CompletionGridCanvas(schema : List<List<Int>>, modifier: Modifier)
{
    Canvas(modifier = modifier) {
        val unitSize = minOf(size.height, size.width)/9

        for(i in 0..8)
        {
            for(j in 0..8)
            {
                if (schema[i][j] != 0)
                    drawRect(
                        color = Color.Green,
                        topLeft = Offset(unitSize*i, unitSize*j),
                        size = Size(unitSize, unitSize),
                        style = Fill
                    )
                else
                    drawRect(
                        color = Color.White,
                        topLeft = Offset(unitSize*i, unitSize*j),
                        size = Size(unitSize, unitSize),
                        style = Fill
                    )
            }


        }

        for(i in 0..9)
        {
            drawLine(
                color = Color.Black,
                start = Offset(unitSize*i, 0F),
                end = Offset(unitSize*i, size.height),
                strokeWidth = 2.5F
            )
        }

        for(j in 0..9)
        {
            drawLine(
                color = Color.Black,
                start = Offset(0F,unitSize*j),
                end = Offset(size.width, unitSize*j),
                strokeWidth = 2.5F
            )
        }
    }
}