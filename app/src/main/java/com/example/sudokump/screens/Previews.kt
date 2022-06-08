package com.example.sudokump.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel

@Preview
@Composable
fun GridPreview(schema : List<List<Int>> = listOf(listOf(0,1,2,3,4,5,6,7,8),listOf(0,1,2,3,4,5,6,7,8),listOf(0,1,2,3,4,5,6,7,8),listOf(0,1,2,3,4,5,6,7,8),listOf(0,1,2,3,4,5,6,7,8),listOf(0,1,2,3,4,5,6,7,8),listOf(0,1,2,3,4,5,6,7,8),listOf(0,1,2,3,4,5,6,7,8),listOf(0,1,2,3,4,5,6,7,8)))
{
    Card(modifier = Modifier.fillMaxWidth()) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

            val (grid) = createRefs()

            CompleteGridCanvas(schema = schema, modifier = Modifier
                .size(100.dp)
                .constrainAs(grid) {
                    top.linkTo(parent.top, 5.dp)
                    start.linkTo(parent.start, 10.dp)
                    end.linkTo(parent.end, 10.dp)
                })
        }
    }
}

@Preview
@Composable
fun CompletedGameCardPW(sudokuGameModel: SudokuGameModel = SudokuGameModel(
    Difficulties.MEDIUM,
    "{\"board\":[[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,6,0],[0,0,0,3,0,0,0,0,0],[2,1,0,0,0,0,0,0,9],[4,5,0,0,0,0,0,0,0],[0,0,8,2,0,3,5,4,0],[6,3,1,0,7,2,0,9,5],[8,4,2,0,3,0,0,0,1],[9,0,0,6,0,1,0,3,0]]}\n")
)
{
    CompletedGameCard(sudokuGameModel = sudokuGameModel)
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun SavedGameCardPW(sudokuGameModel: SudokuGameModel = SudokuGameModel(
    Difficulties.MEDIUM,
    "{\"board\":[[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,6,0],[0,0,0,3,0,0,0,0,0],[2,1,0,0,0,0,0,0,9],[4,5,0,0,0,0,0,0,0],[0,0,8,2,0,3,5,4,0],[6,3,1,0,7,2,0,9,5],[8,4,2,0,3,0,0,0,1],[9,0,0,6,0,1,0,3,0]]}\n")
)
{
    SavedGameCard(sudokuGameModel = sudokuGameModel,0, mutableStateOf(-1))
}