package com.example.sudokump.screens

import android.app.Activity
import android.graphics.Paint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sudokump.R
import com.example.sudokump.common.sqrt
import com.example.sudokump.common.toTime
import com.example.sudokump.modules.ViewModelFactoryProvider
import com.example.sudokump.ui.theme.*
import com.example.sudokump.viewmodel.ActiveGameViewModel
import com.example.sudokump.viewmodel.SudokuTile
import dagger.hilt.android.EntryPointAccessors

enum class ActiveGameScreenState {
    LOADING,
    ACTIVE,
    COMPLETE

}

@Composable
fun ActiveGameScreen(gameId : Int) {

    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).getFactory()

    val viewModel : ActiveGameViewModel = viewModel(factory = ActiveGameViewModel.provideFactory(factory,gameId))

    val contentTransitionState = remember {
        MutableTransitionState(
            ActiveGameScreenState.ACTIVE
        )
    }

    /*viewModel.subContentState = {
        contentTransitionState.targetState = it
    }

    viewModel.onStartBehavior = {
        logic.onEvent(ActiveGameEvent.OnStart)
    }

    viewModel.onStopBehavior = {
        logic.onEvent(ActiveGameEvent.OnStop)
    }

    DisposableEffect(key1 = viewModel) {
        viewModel.onStart()
        onDispose { viewModel.onStop() }
    }*/

    val transition = updateTransition(contentTransitionState, label = "")

    val loadingAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }, label = ""

    ) {
        if (it == ActiveGameScreenState.LOADING) 1f else 0f
    }

    val activeAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }, label = ""

    ) {
        if (it == ActiveGameScreenState.ACTIVE) 1f else 0f
    }

    val completeAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }, label = ""

    ) {
        if (it == ActiveGameScreenState.COMPLETE) 1f else 0f
    }

    Column(
        Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxHeight()


    ) {
        AppToolbar(
            modifier = Modifier.wrapContentHeight(),
            title = stringResource(id = R.string.app_name)
        ){}

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            when (contentTransitionState.currentState) {
                ActiveGameScreenState.ACTIVE -> Box(
                    Modifier.alpha(activeAlpha)
                ) {
                    GameContent(
                        viewModel
                    )
                }
                ActiveGameScreenState.LOADING -> Box(
                    Modifier.alpha(loadingAlpha)
                ) {
                    LoadingScreen()

                }
                ActiveGameScreenState.COMPLETE -> Box(
                    Modifier.alpha(completeAlpha)
                ) {
                    GameCompleteContent(
                        viewModel.timerState,
                        viewModel.isNewRecordedState
                    )
                }
            }
        }
    }
}


@Composable
fun GameContent(
    viewModel: ActiveGameViewModel
) {
    val coordinatePair = rememberSaveable{mutableStateOf(Pair(-1,-1))}
        BoxWithConstraints{
        val screenWidth = with(LocalDensity.current){
            constraints.maxWidth.toDp()
        }

        /*
        Borders margin change based on the screen's density
         */
        val margin = with(LocalDensity.current){
            when{
                constraints.maxHeight.toDp().value < 500 -> 20
                constraints.maxWidth.toDp().value  < 500 -> 8
                else -> 0

            }

        }


        ConstraintLayout {

            val (board , timer , diff , inputs) = createRefs()

            /*
            Now we have to create a container for the puzzle board

             */
            Box(
                Modifier
                    .constrainAs(board) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    }
                    .background(MaterialTheme.colors.surface)
                    .size(screenWidth - margin.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.primaryVariant,

                        ),
            ){
                SudokuBoard(
                    viewModel ,
                    screenWidth - margin.dp,
                    coordinatePair
                )
            }
            
            Row(
                Modifier
                    .wrapContentSize()
                    .constrainAs(diff) {
                        top.linkTo(board.bottom)
                        end.linkTo(parent.end)
                    }
            ){
                (0..viewModel.difficulty.ordinal).forEach{ _ ->
                    Icon(
                        contentDescription = stringResource(R.string.difficulty),
                        imageVector = Icons.Filled.Star,
                        tint = MaterialTheme.colors.secondary,
                        modifier  = Modifier
                            .size(32.dp)
                            .padding(top = 4.dp)

                    )
                }
        }
        Box(
            Modifier
                .wrapContentSize()
                .constrainAs(timer) {
                    top.linkTo(board.bottom)
                    start.linkTo(parent.start)
                }
                .padding(start = 16.dp)

        ){
            TimerText(viewModel)



        }
            /*
            Layout container for the input buttons
            */

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(inputs) {
                    top.linkTo(timer.bottom)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputButtonRow(
                (1..3).toList(),
                coordinatePair,
                viewModel
            )
            InputButtonRow(
                (4..6).toList(),
                coordinatePair,
                viewModel
            )
            InputButtonRow(
                (7..9).toList(),
                coordinatePair,
                viewModel
            )
            InputButtonRow(
                listOf(0),
                coordinatePair,
                viewModel
            )
            NotesButton(viewModel)
            }
        }
    }
}

@Composable
fun NotesButton(viewModel: ActiveGameViewModel) {
    TextButton(
        onClick = {
            viewModel.noteMode.value = !viewModel.noteMode.value
        },
        modifier = Modifier
            .requiredSize(56.dp)
            .padding(2.dp)
            .background(if (viewModel.noteMode.value){Color.White}
            else{Color.Transparent}
            ),
        border = BorderStroke(
            ButtonDefaults.OutlinedBorderSize,
            MaterialTheme.colors.secondary
        )

    ) {
        Image(
            painterResource(R.drawable.ic_notes_foreground), "content description"
        )
    }
}

@Composable
fun SudokuBoard(viewModel: ActiveGameViewModel, size: Dp, coordinatesPair: MutableState<Pair<Int, Int>>)
{
    val boundary = 9

    val tileOffset = size.value /  boundary

    val boardState = viewModel.boardState

    /*viewModel.subBoardState = {
        boardState = it
    }*/

    SudokuTextFields(
        tileOffset,
        boardState,
        coordinatesPair
    )

    BoardGrid(
        boundary,
        tileOffset
    )


}

@Composable
fun SudokuTextFields(
    tileOffset: Float,
    boardState: HashMap<Int, SudokuTile>,
    coordinatesPair: MutableState<Pair<Int, Int>>
) {
    /*
    Here we are going to implement the real grid , where some icons are mutable
     */
    boardState.values.forEach { tile ->
        val text = tile.value.value.toString()
        if (text == "0") {
            SubGrid(Modifier.absoluteOffset(
                (tileOffset * (tile.x.value)).dp,
                (tileOffset * (tile.y.value)).dp,
            ).size(tileOffset.dp)
                .background(if (tile.x.value == coordinatesPair.value.first && tile.y.value == coordinatesPair.value.second) MaterialTheme.colors.secondary.copy(
                    alpha = .25f
                )
                else MaterialTheme.colors.surface)
                .clickable {
                    coordinatesPair.value = Pair(tile.x.value, tile.y.value)
                }, tile.notes)

        } else {
            Text(
                text = text,
                style = mutableSudokuSquare(tileOffset).copy(
                    color = if (MaterialTheme.colors.isLight) {
                        if (tile.readOnly) {
                            Color.Black
                        } else {
                            userInputtedNumberLight
                        }
                    } else {
                        if (tile.readOnly) {
                            MaterialTheme.colors.onSecondary
                        } else {
                            userInputtedNumberLight
                        }
                    }
                ),
                modifier = Modifier
                    .absoluteOffset(
                        (tileOffset * (tile.x.value)).dp,
                        (tileOffset * (tile.y.value)).dp,
                    )
                    .size(tileOffset.dp)
                    .background(
                        if (tile.x.value == coordinatesPair.value.first && tile.y.value == coordinatesPair.value.second) MaterialTheme.colors.secondary.copy(
                            alpha = .25f
                        )
                        else MaterialTheme.colors.surface
                    )
                    .clickable {
                        coordinatesPair.value = Pair(tile.x.value, tile.y.value)
                    }
            )
        }
    }
}

@Composable
fun InputButtonRow(
    numbers: List<Int>,
    coordinatesPair: MutableState<Pair<Int, Int>>,
    viewModel: ActiveGameViewModel
) {
    Row{
        numbers.forEach{
            SudokuInputButton(
                coordinatesPair,
                it,
                viewModel
            )
        }
    }

    // Add space in the layout
    Spacer(Modifier.size(2.dp))
}

@Composable
fun SudokuInputButton(
    coordinatesPair: MutableState<Pair<Int, Int>>,
    number: Int,
    viewModel: ActiveGameViewModel
) {
    TextButton(
        onClick = {
            viewModel.updateNode(
            coordinatesPair.value.first,
            coordinatesPair.value.second,
            number
            )
            viewModel.overallCheck()
          },
        modifier = Modifier
            .requiredSize(56.dp)
            .padding(2.dp),
        border = BorderStroke(
            ButtonDefaults.OutlinedBorderSize,
            MaterialTheme.colors.secondary
        )

    ) {
        Text(
            text = number.toString(),
            style = inputButton.copy(color = MaterialTheme.colors.secondary),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun BoardGrid(boundary: Int, tileOffset: Float) {
    (1 until boundary).forEach { it ->
        val width = if (it % boundary.sqrt == 0) 3.dp
        else 1.dp

        Divider(
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
                .absoluteOffset((tileOffset * it).dp, 0.dp)
                .fillMaxHeight()
                .width(width)
        )

        (1 until boundary).forEach {
            val height = if (it % boundary.sqrt == 0) 3.dp
            else 1.dp

            Divider(
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier
                    .absoluteOffset(0.dp, (tileOffset * it).dp)
                    .fillMaxWidth()
                    .height(height)
            )
        }
    }
}


@Composable
fun TimerText(viewModel: ActiveGameViewModel) {

    val timerState by remember {
        mutableStateOf("")
    }

    /*viewModel.subTimerState = {
        timerState = it.toTime()
    }*/

    Text(
        modifier = Modifier.requiredHeight(36.dp),
        text = timerState,
        style = activeGameSubtitle.copy(color = MaterialTheme.colors.secondary)
    )
}

@Composable
fun GameCompleteContent(timerState: Long , isNewRecordState: Boolean) {
    /*
    The composable for the case when the user effectively completes a game
     */

    Column (
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ){
            Image(
                contentDescription = stringResource(R.string.game_complete),
                imageVector = Icons.Filled.Star,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
                modifier = Modifier.size(128.dp)
            )

            if (isNewRecordState) Image(
                contentDescription = null,
                imageVector = Icons.Filled.Star,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
                modifier = Modifier.size(128.dp)

            )
        }

        Text(
            text = stringResource(R.string.total_time),
            style = newGameSubtitle.copy(
                color = MaterialTheme.colors.secondary
            )

        )

        Text(
            text = timerState.toTime(),
            style = newGameSubtitle.copy(
                color = MaterialTheme.colors.secondary
            )
        )
    }
}

@Composable
fun SubGrid(modifier: Modifier, array: Array<MutableState<Boolean>>){
    Canvas(modifier = modifier
    )
    {
        val unitSize = minOf(size.height, size.width)/3

        for(j in 0..8)
        {
            drawIntoCanvas {
                val textPaint = Paint().apply {
                    textSize = (unitSize/3).sp.toPx()
                }
                it.nativeCanvas.drawText(
                    "${if(array[j].value){
                        j+1
                    } else {""}
                    }",
                    unitSize* (j%3)  + (unitSize/12).dp.toPx(),
                    unitSize* (j/3) + (unitSize/3).dp.toPx(),
                    textPaint
                )
            }
        }
    }
}


