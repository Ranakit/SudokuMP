package com.example.sudokump.ui.theme.activeGame

import com.example.sudokump.common.BaseLogic
import com.example.sudokump.common.DispatcherProvider
import com.example.sudokump.computationlogic.nodeMapToTileMap
import com.example.sudokump.domain.IGameRepository
import com.example.sudokump.domain.IStatisticsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/*
Presentation logic for this particular function
 */
class ActiveGameLogic (
    private val container: ActiveGameContainer?,
    private val viewModel: ActiveGameViewModel,
    private val gameRepo: IGameRepository,
    private val statsRepo: IStatisticsRepository,
    private val dispatcher: DispatcherProvider
): BaseLogic<ActiveGameEvent>() , CoroutineScope {
    override val coroutineContext : CoroutineContext
        get() = dispatcher.provideUIContext() + jobTracker

    init {
        jobTracker = Job()

    }

    /*
    Now we have to implement a timer for the coroutine , called coroutine timer.

     */


    /*
    In this function , a loop is recalled every 1000 ms:
    - with this delay we will not block the coroutine , but delaying it so it can still work on the thread;
    - action is a lambda expression that we will pass to this function
    So , this function provide a security mechanism in the case when is returned something different from
    the lambda expression passed as an argument

     */
    private val Long.timeOffset: Long
        get() {
            return if (this <= 0) 0
            else this - 1
        }
    private inline fun startCoroutineTimer(
        crossinline action : () -> Unit

    ) = launch {
        while (true){
            action()
            delay(1000)
        }
    }

    private var timerTracker: Job? = null

    /*
     Instead of implementing a single function that describes a single event, below we
     implement a single function capable of handling multiple events by passing the single argument ActiveGameEvent

     */
    override fun onEvent(event: ActiveGameEvent) {
        when(event){
            is ActiveGameEvent.OnInput -> onInput(
                event.input,
                viewModel.timerState
            )
            ActiveGameEvent.OnNewGameClicked -> onNewGameClicked()
            ActiveGameEvent.OnStart -> onStart()
            ActiveGameEvent.OnStop -> onStop()
            is ActiveGameEvent.OnTileFocused -> onTileFocused(event.x, event.y)
        }

    }

    private fun onTileFocused(x : Int , y : Int) {
        viewModel.updateFocusState(x , y)
    }

    private fun onStop() {
        /*
        This function is called when a user stop the game and the app kind to be shutted down

         */

        if (!viewModel.isCompleteState){
            launch {
                gameRepo.saveGame(
                    viewModel.timerState.timeOffset,
                    nodeMapToTileMap(viewModel.boardState),
                    viewModel.difficulty,
                    {},
                    {}
                    )}
        }
        else
            deleteStuff()
    }


    private fun onStart() = launch {
        gameRepo.getCurrentGame({
            puzzle, isComplete ->
        viewModel.initializeBoardState(
            puzzle,
            isComplete
        )
        if (!isComplete) timerTracker = startCoroutineTimer {
            viewModel.updateTimerState()
        }
    },
    {})
}

    private fun onNewGameClicked() = launch {
        viewModel.showLoadingState()

        if(!viewModel.isCompleteState){
            gameRepo.getCurrentGame(
                { _, _ ->
                    updateWithTime()
                }
            ) {
                container?.showErr()

            }
        } else {
            //openNewGame()
        }
    }

    private fun updateWithTime() = launch {

    }

    /*private fun openNewGame() {

        deleteStuff()
        container?.openNewGame()

        TODO("Not yet implemented")
    }*/

    private fun deleteStuff() {
        if (timerTracker?.isCancelled == false) timerTracker?.cancel()

        jobTracker.cancel()

    }

    private fun onInput(input: Int , elapsedTime : Long  ) = launch {
        var focusedTile: SudokuTile? = null
        viewModel.boardState.values.forEach{
            if(it.hasFocus) focusedTile = it
        }

        if (focusedTile != null){
            gameRepo.updateNode(
                focusedTile !!.x,
                focusedTile !!.y,
                input ,
                elapsedTime,
                /*
                Creating the lambda expression for the success case
                 */
                { isComplete ->
                    focusedTile?.let {
                        viewModel.updateBoardState(
                            it.x,
                            it.y,
                            input,
                            false,
                            false
                            )
                    }

                    if (isComplete){
                        timerTracker?.cancel()
                        checkNewRecord()

                    }

                },

                /*
                Error case
                 */
                { container?.showErr()}
            )
        }
    }

    private fun checkNewRecord() = launch {
        statsRepo.updateStatistic(
            viewModel.timerState,
            viewModel.difficulty,
            viewModel.boundary,
            // Success case
            {
                    isRecord ->
                viewModel.isNewRecordedState = isRecord
                viewModel.updateCompleteState()
            },

            // Error case

            {
                container?.showErr()
                viewModel.updateCompleteState()
            }

        )
    }

}