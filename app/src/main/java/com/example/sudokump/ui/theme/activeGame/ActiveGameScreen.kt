package com.example.sudokump.ui.theme.activeGame

import android.provider.BlockedNumberContract
import android.widget.Space
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.sudokump.R
import com.example.sudokump.common.toTime
import com.example.sudokump.model.SudokuBoard
import com.example.sudokump.ui.theme.*
import java.lang.reflect.Modifier
import java.util.HashMap

enum class ActiveGameScreenState {
    LOADING,
    ACTIVE,
    COMPLETE

}


@Composable
fun ActiveGameScreen(
    onEventHandler: (ActiveGameEvent) -> Unit,
    viewModel: ActiveGameViewModel
) {
    val contentTransitionState = remember {
        MutableTransitionState(
            ActiveGameScreenState.LOADING
        )
    }

    viewModel.subContentState = {
        contentTransitionState.targetState = it
    }


    val transition = updateTransition(contentTransitionState)

    val loadingAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }

    ){
        if (it == ActiveGameScreenState.LOADING) 1f else 0f
    }

    val activeAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }

    ){
        if (it == ActiveGameScreenState.ACTIVE) 1f else 0f
    }

    val completeAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }

    ){
        if (it == ActiveGameScreenState.COMPLETE) 1f else 0f
    }

    Column(
        androidx.compose.ui.Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxHeight()


        ){
            AppToolbar(
                modifier = androidx.compose.ui.Modifier.wrapContentHeight(),
                title = stringResource(id = R.string.app_name))
            {
                NewGameIcon(onEventHandler = onEventHandler)
            }

            Box(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxHeight()
                    .padding(top = 4.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                when (contentTransitionState.currentState) {
                    ActiveGameScreenState.ACTIVE ->Box(
                        androidx.compose.ui.Modifier.alpha(activeAlpha)
                    ) {
                        GameContent(
                            onEventHandler,
                            viewModel
                        )
                }
                    ActiveGameScreenState.LOADING -> Box (
                        androidx.compose.ui.Modifier.alpha(loadingAlpha)
                    ){
                        LoadingScreen()

                    }
                    ActiveGameScreenState.COMPLETE -> Box(
                        androidx.compose.ui.Modifier.alpha(completeAlpha)
                    ) {
                        GameCompleteContent(
                            viewModel.timerState,
                            viewModel.isNewRecordedState
                        )
                    }
                    else -> {}
                }
        }
    }

@Composable
fun NewGameIcon(onEventHandler: (ActiveGameEvent) -> Unit) {
    Icon(
        imageVector = Icons.Filled.Add,
        tint = if (MaterialTheme.colors.isLight) textColorLight else
            textColorDark,
        contentDescription = null,
        modifier = Modifier
            .clickable() {
                onEventHandler.invoke(ActiveGameEvent.OnNewGameClicked)
            }
            .padding(horizontal = 20.dp , vertical = 20.dp)
            .height(40.dp)
    )

}




}


@Composable
fun GameContent(
    onEventHandler: (ActiveGameEvent) -> Unit,
    viewModel: ActiveGameViewModel
) {
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
            /*
            In order to contraint the compasable to each other we need to reference each other
             */

            val (board , timer , diff , inputs) = createRefs()

            /*
            Now we have to create a container for the puzzle board

             */
            Box(
                androidx.compose.ui.Modifier
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
                    onEventHandler,
                    viewModel ,
                    screenWidth - margin.dp

                )
            }
            
            Row(
                androidx.compose.ui.Modifier
                    .wrapContentSize()
                    .constrainAs(diff) {
                        top.linkTo(board.bottom)
                        end.linkTo(parent.end)
                    }
            ){
                (0..viewModel.difficulty.ordinal).forEach{
                    Icon(
                        contentDescription = stringResource(R.string.difficulty),
                        imageVector = Icons.Filled.Star,
                        tint = MaterialTheme.colors.secondary,
                        modifier  = androidx.compose.ui.Modifier
                            .size(32.dp)
                            .padding(top = 4.dp)

                    )
                }
        }
        Box(
            androidx.compose.ui.Modifier
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
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(inputs) {
                    top.linkTo(timer.bottom)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.boundary == 4){
                InputButtonRow(
                    (0..4).toList(),
                    onEventHandler
                )

                InputButtonRow(
                    (5..9).toList(),
                    onEventHandler
                )
            }
        }


    }
    
}


@Composable
fun SudokuBoard(onEventHandler: (ActiveGameEvent) -> Unit,
                viewModel: ActiveGameViewModel,
                dp: Dp)
{
    val boundary = viewModel.boundary

    val tileOffset = size.value /  boundary

    var boardState by remember {
        mutableStateOf(viewModel.boardState , neverEqualPolicy())

    }

    viewModel.subBoardState = {
        boardState = it
    }

    SudokuTextFields(
        onEventHandler,
        tileOffset,
        boardState
    )

    BoardGrid(
        boundary,
        tileOffset
    )


}

@Composable
fun SudokuTextFields(
    onEventHandler: (ActiveGameEvent) -> Unit,
    tileOffset: Any,
    boardState: HashMap<Int, SudokuTile>
) {

        /*
        Here we are going to implement the real grid , where some icons are mutable
         */
        boardState.values.forEach { _ ->
            var text = tile.value.toString()

            if (!tile.readOnly) {
                if (text == "0") text = ""
                Text(
                    text = text,
                    style = mutableSudokuSquare(tileOffset).copy(
                        color = if (MaterialTheme.colors.isLight) userInputtedNumberLight
                        else userInputtedDark
                    ),
                    modifier = androidx.compose.ui.Modifier
                        .absoluteOffset(
                            (tileOffset * (tile.x - 1)).dp,
                            (tileOffset * (tile.y - 1)).dp,


                            )
                        .size(tileOffset.dp)
                        .background(
                            if (tile.hasFocus) MaterialTheme.colors.onPrimary.copy(alpha = .25f)
                            else MaterialTheme.colors.surface
                        )
                        .clickable {
                            onEventHandler.invoke(
                                ActiveGameEvent.OnTileFocused(tile.x, tile.y)
                            )
                        }

                )
            } else {
                Text(
                    text = text,
                    style = readOnlySudokuSquare(
                        tileOffset
                    ),
                    modifier = androidx.compose.ui.Modifier
                        .absoluteOffset(

                            (tileOffset * (tile.x - 1)).dp,
                            (tileOffset * (tile.y - 1)).dp,

                            )


                )

            }

        }
    }
}

@Composable
fun InputButtonRow(numbers: List<Int>, onEventHandler: (ActiveGameEvent) -> Unit) {
    Row{
        numbers.forEach{
            SudokuInputButton(
                onEventHandler,
                it
            )
        }
    }

    // Add space in the layout
    Spacer(androidx.compose.ui.Modifier.size(2.dp))
}

@Composable
fun SudokuInputButton(onEventHandler: (ActiveGameEvent) -> Unit, number: Int) {
    TextButton(
        onClick = { onEventHandler.invoke(ActiveGameEvent.OnInput(number)) },
        modifier = androidx.compose.ui.Modifier
            .requiredSize(56.dp)
            .padding(2.dp),
        border = BorderStroke(
            ButtonDefaults.OutlinedBorderSize,
            MaterialTheme.colors.onPrimary
        )

    ) {
        Text(
            text = number.toString(),
            style = inputButton.copy(color = MaterialTheme.colors.onPrimary),
            modifier = androidx.compose.ui.Modifier.fillMaxSize()
        )
    }
}

@Composable
fun TimerText(viewModel: ActiveGameViewModel) {
    var timerState by remember {
        mutableStateOf("")

    }

    viewModel.timerState ={
        timerState = it.toTime()

    }


    Text(
        modifier = androidx.compose.ui.Modifier.requiredHeight(36.dp),
        text = timerState,
        style = activeGameSubtitle.copy(
            color = MaterialTheme.colors.secondary
        )
    )
}

@Composable
fun BoardGrid(boundary: Int, tileOffset: Float) {
    (1 until boundary).forEach {
        val width = if (it % boundary.sqrt == 0) 3.dp
        else 1.dp

        Divider(
            color = MaterialTheme.colors.primaryVariant,
            modifier = androidx.compose.ui.Modifier
                .absoluteOffset((tileOffset * it).dp, 0.dp)
                .fillMaxHeight()
                .width(width)
        )

        (1 until boundary).forEach {
            val height = if (it % boundary.sqrt == 0) 3.dp
            else 1.dp

            Divider(
                color = MaterialTheme.colors.primaryVariant,
                modifier = androidx.compose.ui.Modifier
                    .absoluteOffset((tileOffset * it).dp, 0.dp)
                    .fillMaxWidth()
                    .height(width)
            )

        }
    }
}

@@Composable
fun GameCompleteContent(timerState: Long , isNewRecordState: Boolean) {
    /*
    The composable for the case when the user effectively completes a game

     */
    Column (
        androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Box(
            modifier = androidx.compose.ui.Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ){
            Image(
                contentDescription = stringResource(R.string.game_complete),
                imageVector = Icons.Filled.EmojiEvents,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                modifier = androidx.compose.ui.Modifier.size(128.dp)
            )

            if (isNewRecordState) Image(
                contentDescription = null,
                imageVector = Icons.Filled.Star,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                modifier = androidx.compose.ui.Modifier.size(128.dp)

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



