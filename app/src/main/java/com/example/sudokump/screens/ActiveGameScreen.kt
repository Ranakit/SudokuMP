package com.example.sudokump.screens

import android.app.Activity
import android.graphics.Paint
import android.widget.Toast
import androidx.compose.animation.core.*
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
import androidx.navigation.NavController
import com.example.sudokump.R
import com.example.sudokump.common.sqrt
import com.example.sudokump.modules.ViewModelFactoryProvider
import com.example.sudokump.ui.theme.*
import com.example.sudokump.viewmodel.ActiveGameViewModel
import com.example.sudokump.viewmodel.HintNotFoundException
import dagger.hilt.android.EntryPointAccessors

enum class ActiveGameScreenState {
    LOADING,
    ACTIVE,
    COMPLETE
}

@Composable
fun ActiveGameScreen(gameId : Int, navController: NavController) {

    val contentTransitionState = remember {
        MutableTransitionState(
            ActiveGameScreenState.LOADING
        )
    }

    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).getFactory()

    val viewModel : ActiveGameViewModel = viewModel(factory = ActiveGameViewModel.provideFactory(factory,gameId))

    DisposableEffect(key1 = viewModel) {
        viewModel.onStart()
        contentTransitionState.targetState = ActiveGameScreenState.ACTIVE
        onDispose { viewModel.onStop(navController) }
    }

    val transition = updateTransition(contentTransitionState, label = "")

    val loadingAlpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 1500)
        }, label = ""

    ) {
        if (it == ActiveGameScreenState.LOADING) 1000f else 0f
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
                        viewModel,
                        contentTransitionState
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
                        viewModel
                    )
                }
            }
        }
    }
}


@Composable
fun GameContent(
    viewModel: ActiveGameViewModel, contentTransitionState : MutableTransitionState<ActiveGameScreenState>
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

            val (board , timer , diff , inputs, completionPercent) = createRefs()

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
                Text(
                    text = viewModel.timer.value.toComponents {hours, minutes, seconds, _ ->
                            "$hours:$minutes:$seconds"
                    },
                    style = newGameSubtitle.copy(
                        color = MaterialTheme.colors.secondary
                    )
                )
            }

            Box(
                Modifier
                .wrapContentSize()
                .constrainAs(completionPercent) {
                    top.linkTo(board.bottom)
                    start.linkTo(timer.end)
                    end.linkTo(diff.start)
                }
                .padding(start = 16.dp)) {

                Text(
                    text = viewModel.completionPercent.value ,
                    style = newGameSubtitle.copy(
                        color = MaterialTheme.colors.secondary
                    )
                )
            }

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
                    viewModel,
                    contentTransitionState
                )
                InputButtonRow(
                    (4..6).toList(),
                    coordinatePair,
                    viewModel,
                    contentTransitionState
                )
                InputButtonRow(
                    (7..9).toList(),
                    coordinatePair,
                    viewModel,
                    contentTransitionState
                )
                Row {
                    ClearButton(viewModel, coordinatePair)
                    NotesButton(viewModel)
                    HintButton(viewModel,contentTransitionState)
                }
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
            .background(
                if (viewModel.noteMode.value) {
                    Color.White
                } else {
                    Color.Transparent
                }
            ),
        border = BorderStroke(
            ButtonDefaults.OutlinedBorderSize,
            MaterialTheme.colors.secondary
        )

    ) {
        Image(
            painterResource(R.drawable.ic_notes), "content description"
        )
    }
}

@Composable
fun ClearButton(viewModel: ActiveGameViewModel, coordinatePair: MutableState<Pair<Int, Int>>) {
    TextButton(
        onClick = {
            viewModel.clearTile(coordinatePair)
        },
        modifier = Modifier
            .requiredSize(56.dp)
            .padding(2.dp)
            .background(
                Color.Transparent
            ),
        border = BorderStroke(
            ButtonDefaults.OutlinedBorderSize,
            MaterialTheme.colors.secondary
        )

    ) {
        Image(
            painterResource(R.drawable.ic_gomma), "content description"
        )
    }
}

@Composable
fun HintButton(viewModel: ActiveGameViewModel, contentTransitionState: MutableTransitionState<ActiveGameScreenState>) {
    val context = LocalContext.current
    TextButton(onClick = {
        try {
            viewModel.findHint()
            if(viewModel.overallCheck()) {
                contentTransitionState.targetState = ActiveGameScreenState.COMPLETE
            }
        }catch (e : HintNotFoundException) {
            Toast.makeText(context, "Hint not found, please look after errors", Toast.LENGTH_LONG).show()
        }},
        modifier = Modifier
            .requiredSize(56.dp)
            .padding(2.dp)
            .background(
                Color.Transparent
            ),
        border = BorderStroke(
            ButtonDefaults.OutlinedBorderSize,
            MaterialTheme.colors.secondary
        )) {
        Image(
            painterResource(R.drawable.ic_hint_24), "Get a hint"
        )
    }
}

@Composable
fun SudokuBoard(viewModel: ActiveGameViewModel, size: Dp, coordinatesPair: MutableState<Pair<Int, Int>>)
{
    val boundary = 9

    val tileOffset = size.value /  boundary

    SudokuTextFields(
        tileOffset,
        viewModel,
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
    viewModel: ActiveGameViewModel,
    coordinatesPair: MutableState<Pair<Int, Int>>
) {
    /*
    Here we are going to implement the real grid , where some icons are mutable
     */
    viewModel.boardState.values.forEach { tile ->
        val text = tile.value.value.toString()
        if (text == "0") {
            SubGrid(
                Modifier
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
                    }, tile.notes)

        } else {
            Text(
                text = text,
                style = mutableSudokuSquare(tileOffset).copy(
                    color = if (MaterialTheme.colors.isLight) {
                        if (tile.readOnly) {
                            Color.Black
                        } else if(!viewModel.checkTileIsCorrect(tile.x.value, tile.y.value)) {
                            userInputtedNumberWrong
                        } else {
                            userInputtedNumberLight
                        }
                    } else {
                        if (tile.readOnly) {
                            MaterialTheme.colors.onSecondary
                        } else if(!viewModel.checkTileIsCorrect(tile.x.value, tile.y.value)) {
                            userInputtedNumberWrong
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
    viewModel: ActiveGameViewModel,
    contentTransitionState : MutableTransitionState<ActiveGameScreenState>
) {
    Row{
        numbers.forEach{
            SudokuInputButton(
                coordinatesPair,
                it,
                viewModel,
                contentTransitionState
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
    viewModel: ActiveGameViewModel,
    contentTransitionState : MutableTransitionState<ActiveGameScreenState>
) {
    TextButton(
        onClick = {
            viewModel.updateNode(
            coordinatesPair.value.first,
            coordinatesPair.value.second,
            number
            )
            if(viewModel.overallCheck())
            {
                contentTransitionState.targetState = ActiveGameScreenState.COMPLETE
            }
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
fun GameCompleteContent(viewModel: ActiveGameViewModel) {
    viewModel.stopTimer()

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

            if (viewModel.isNewRecordedState) Image(
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
            text = viewModel.timer.value.toComponents { hours, minutes, seconds, _ ->
             "$hours:$minutes:$seconds"
            },
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


