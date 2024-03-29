package com.example.sudokump.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sudokump.R
import com.example.sudokump.common.mapToList
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.viewmodel.CompletedGamesScreenViewModel

@Composable
fun CompletedGamesScreen(viewModel: CompletedGamesScreenViewModel) {

    val savedGames = viewModel.completedGames
    val sortedList = savedGames.sortedBy { it.timePassed }
    var i = 0
    Column{
        Text(text = stringResource(id = R.string.completed_games_list))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sortedList.size)
            {
                CompletedGameCard(sortedList[it], i)
                i++
            }
        }
    }
}


@Composable
fun CompletedGameCard(sudokuGameModel: SudokuGameModel,int: Int)
{
    val painter : Painter = when (int) {
        0 -> painterResource(R.drawable.ic_cup_first)
        1 ->  painterResource(R.drawable.ic_cup_second)
        2 -> painterResource(R.drawable.ic_cup_third)
        else -> ColorPainter(MaterialTheme.colors.primary)
    }
    Card(
        modifier = Modifier.fillMaxWidth()) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

            val (grid, id, timePassed, completionPercent, difficulty, saveDate) = createRefs()

            Image(painter = painter  , contentDescription = "")

            CompleteGridCanvas(schema = mapToList(sudokuGameModel.schema.map), modifier = Modifier
                .size(150.dp)
                .constrainAs(grid) {
                    top.linkTo(parent.top, 5.dp)
                    start.linkTo(parent.start, 10.dp)
                    end.linkTo(parent.end, 10.dp)
                })

            Text("${stringResource(id = R.string.id)}: ${sudokuGameModel.id}", modifier = Modifier.constrainAs(id){
                top.linkTo(grid.bottom, 5.dp)
                start.linkTo(parent.start, 4.dp)
                end.linkTo(completionPercent.start)
            })

            Text("${stringResource(id = R.string.timePassed)}: ${sudokuGameModel.timePassed}", modifier = Modifier.constrainAs(timePassed){
                top.linkTo(id.bottom, margin = 5.dp)
                start.linkTo(parent.start, 4.dp)
                })

            Text("${stringResource(id = R.string.difficulty)}: ${sudokuGameModel.difficulty}", modifier = Modifier.constrainAs(difficulty){
                top.linkTo(id.top)
                start.linkTo(id.end, 20.dp)
            })

            Text("${stringResource(id = R.string.savedWhen)}: ${sudokuGameModel.saveDate.dayOfMonth}/${sudokuGameModel.saveDate.monthValue}/${sudokuGameModel.saveDate.year}",
                modifier = Modifier.constrainAs(saveDate){
                    top.linkTo(timePassed.top)
                    start.linkTo(timePassed.end, 20.dp)
                })
        }
    }
}

@Composable
fun CompleteGridCanvas(schema : List<List<Int>>, modifier: Modifier)
{
    Canvas(modifier = modifier) {
        val unitSize = minOf(size.height, size.width)/9

        for(i in 0..8)
        {
            for(j in 0..8)
            {
                drawRect(
                    color = Color.White,
                    topLeft = Offset(unitSize*i, unitSize*j),
                    size = Size(unitSize, unitSize),
                    style = Fill
                )

                drawIntoCanvas {
                    val textPaint = Paint().apply {
                        textSize = (unitSize/3).sp.toPx()
                    }
                    it.nativeCanvas.drawText(
                        "${schema[i][j]}",
                        unitSize*i + (unitSize/12).dp.toPx(),
                        unitSize*(j+1)- (unitSize/15).dp.toPx(),
                        textPaint
                    )
                }
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