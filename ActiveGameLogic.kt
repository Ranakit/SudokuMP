package com.fabrizio.sudoku.ui.activegame

import com.fabrizio.sudoku.common.BaseLogic
import com.fabrizio.sudoku.domain.IGameRepository
import com.fabrizio.sudoku.domain.IStatisticsRepository
import com.fabrizio.sudoku.domain.SudokuPuzzle
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
    private val dispatcher: Disp atcherProvider


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
    inline fun startCoroutineTimer(
        crossinline action : () -> Unit

    ) = launch {
        while (true){
            action()
            delay(1000)
        }
    }


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
            is ActiveGameEvent.OnTileFocused -> onTileFocused()
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
                    {deleteStuff()},
                    {
                        deleteStuff()
                        container?.showErr()
                    }
                )
            }
        }
        else
            deleteStuff()
    }


    private fun onStart() = launch {
        gameRepo.getCurrentGame(
            { puzzle, isComplete ->
                viewModel.initializeBoard(
                    puzzle,
                    isComplete
                )
                if (!isComplete) timerTracker = startCoroutineTimer {
                    viewModel.updateTimerState()

                }

            }
            {
                container?.onNewGameClick()
            }

        )
    }

    private fun onNewGameClicked() = launch {
        viewModel.showLoadingState()


        /*
        We're asking to the view model if the user has completed the current game ,
        and if not we have to store the current progresss.
        This has to happen even if the users clicked  for accident on a new game button or they're trying
        to ga back through the app.
        So in this particular case the progresses has to be saved.
        */
        if(!viewModel.isCompleteState){
            gameRepo.getCurrentGame(
                { puzzle, _ ->
                    updateWithTime(puzzle)
                }

            ) {
                container?.showErr()

            }
        } else {
            openNewGame()

        }
    }

    private fun updateWithTime(puzzle: SudokuPuzzle)  = launch{

        gameRepo.updateGame(
            puzzle.copy(elapsedTime = viewModel.timerState.timeOffset),
            // Success case
            {
                openNewGame()

            },
            //Errore case
            {
                container?.showErr()
                openNewGame()
            }

        )

    }

    private fun openNewGame() {

        deleteStuff()
        container?.openNewGame()

        TODO("Not yet implemented")
    }

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
                Creating the lambda ecxpression for the success case
                 */
                { isComplete ->
                    focusedTile?.let {
                        viewModel.updateBoardState(
                            it.x,
                            it.y,
                            input,
                            false,

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