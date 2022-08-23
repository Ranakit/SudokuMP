package com.example.sudokump.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.model.SudokuHintFinder
import com.example.sudokump.model.getHash
import com.example.sudokump.modules.ActiveGameViewModelAssistedFactory
import com.example.sudokump.modules.SudokuGamesEntryPoint
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class ActiveGameViewModel @AssistedInject constructor(
    @Assisted id : Int, @ApplicationContext context: Context) : ViewModel() {

    private lateinit var game : SudokuGameModel
    private lateinit var entryPoint : SudokuGamesEntryPoint
    val boardState = HashMap<Int, SudokuTile>()
    var noteMode: MutableState<Boolean> = mutableStateOf(false)
    val isNewRecordedState = true
    val id: Int
    val context: Context
    private lateinit var timerJob : Job
    lateinit var timer : MutableState<Duration>
    lateinit var completionPercent : MutableState<String>
    val isHintPopupVisible : MutableState<Boolean> = mutableStateOf(false)
    private var hintFinder : SudokuHintFinder? = null

    val difficulty : Difficulties
    get()
    {
        return game.difficulty
    }

    companion object{
        fun provideFactory(
            assistedFactory: ActiveGameViewModelAssistedFactory,
            gameId : Int
        ) : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(gameId) as T
            }
        }
    }

    init {
        this.id = id
        this.context = context
    }

    fun overallCheck() : Boolean{
        return game.schema.overallCheck() && game.schema.overallCompletenessCheck()
    }

    fun updateNode(tileX : Int, tileY : Int , value : Int){
        if (tileX < 0) {
            return
        }
        val tile = game.schema.map[getHash(tileX, tileY)]
        val board = boardState[getHash(tileX, tileY)]
        if (tile != null) {
            if (tile.readOnly){
                return
            }
        }
        if (noteMode.value){
            tile?.notes?.add(value)
            board?.notes?.get(value-1)?.value = !(board?.notes?.get(value-1)?.value)!!
        }
        else {
            tile?.setValue(value)
            board?.value?.value = value
            completionPercent.value = game.evaluateCompletionPercent()
        }
    }

    private fun notesControl(set : MutableSet<Int>) : Array<MutableState<Boolean>>{
        val array: Array<MutableState<Boolean>> = Array(9){
            if (set.contains(it+1))
            {
                mutableStateOf(true)
            }
            else
            {
                mutableStateOf(false)
            }
        }

        return array
    }

    fun clearTile(coordinatePair: MutableState<Pair<Int,Int>>){
        if (coordinatePair.value.first < 0) {
            return
        }
        val tile = game.schema.map[getHash(coordinatePair.value.first, coordinatePair.value.second)]
        if (tile!!.readOnly){
            return
        }
        val board = boardState[getHash(coordinatePair.value.first, coordinatePair.value.second)]
        tile.setValue(0)
        board?.value?.value = 0
        completionPercent.value = game.evaluateCompletionPercent()
        for (i in 0..8){
            tile.notes.clear()
            board?.notes?.get(i)?.value = false
        }
    }

    fun findHint() {
        val hintFinder = SudokuHintFinder(game.schema.copy())
        hintFinder.executeAsyncReturnTask()

        this.hintFinder = hintFinder
    }

    fun putHint() {
        if (this.hintFinder == null) throw HintNotFoundException()
        val hint = this.hintFinder!!.joinTask()
        if(hint.first != null)
        {
            val nodeToUpdate = game.schema.map[getHash(hint.second!!.x, hint.second!!.y)]
            nodeToUpdate?.setValue(hint.first!!)
            val tileToUpdate = boardState[getHash(hint.second!!.x, hint.second!!.y)]
            tileToUpdate?.value?.value = hint.first!!
            completionPercent.value = game.evaluateCompletionPercent()
            timer.value = game.timePassed.plus(10.seconds)
            game.timePassed = timer.value
        } else throw HintNotFoundException()
    }

    fun checkTileIsCorrect(tileX : Int, tileY : Int) : Boolean {
        val node = game.schema.map[getHash(tileX, tileY)]
        return node!!.isCorrect
    }

    fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.IO).launch(start = CoroutineStart.LAZY) {
            val start = LocalDateTime.now()
            while (isActive) {
                val newTimer = start.until(LocalDateTime.now(), ChronoUnit.SECONDS)
                timer.value = game.timePassed.plus(newTimer.seconds)
            }
            game.timePassed = timer.value
        }
        timerJob.start()
    }

    fun stopTimer() {
        timerJob.cancel()
    }

    fun onStart() {
        entryPoint = EntryPointAccessors.fromApplication(context, SudokuGamesEntryPoint::class.java)

        game = when{
            id == -2 -> entryPoint.getNewEasyGame()
            id == -1 -> entryPoint.getNewMediumGame()
            id == 0 -> entryPoint.getNewHardGame()
            id > 0 -> SudokuGameModel.getFromDB(context, id)
            id == -3 -> SudokuGameModel.getFromDB(context, id)
            else -> entryPoint.getNewEasyGame()
        }

        for(i in 0..8)
        {
            for(j in 0..8)
            {
                val node = game.schema.map[getHash(i,j)]
                boardState[getHash(i,j)] = SudokuTile(mutableStateOf(node!!.x), mutableStateOf(node.y), mutableStateOf(node.getValue()), node.readOnly, notesControl(node.notes))
            }
        }
        completionPercent = mutableStateOf(game.evaluateCompletionPercent())
        timer = mutableStateOf(game.timePassed)
        startTimer()
    }

    fun onStop(navController: NavController) {
        stopTimer()
        game.saveInDB(context)
        navController.backQueue.clear()
        navController.navigate("Init")
    }
}

class HintNotFoundException : Exception()