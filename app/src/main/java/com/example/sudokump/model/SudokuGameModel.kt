package com.example.sudokump.model

import android.content.Context
import com.example.sudokump.persistency.dao.SavedGamesDAO
import com.example.sudokump.persistency.entities.SavedGameDBEntity
import com.google.gson.Gson
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit


class SudokuGameModel {
    val id : Int
    val timePassed  : Duration
    val completionPercent : String
    val schema : List<List<Int>>
    val difficulty : Difficulties
    val saveDate : LocalDate

    constructor(savedGameDBEntity: SavedGameDBEntity) {
        id = savedGameDBEntity.id
        timePassed  = savedGameDBEntity.secondsPassed.seconds
        completionPercent = savedGameDBEntity.completionPercent
        difficulty = Difficulties.valueOf(savedGameDBEntity.difficulty) //Will be assured to be saved correctly to DB
        //Will be assured to be saved correctly to DB
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        saveDate = LocalDate.parse(savedGameDBEntity.savedDate, formatter)

        var json = String(savedGameDBEntity.savedSchema)
        json = json.substring(0, json.length -1)
        schema = extractBoardFromJson(json)
    }

    constructor(gameDifficulty: Difficulties, json: String)
    {
        id = 0
        timePassed = Duration.ZERO
        difficulty = gameDifficulty
        saveDate = LocalDate.now()

        schema = extractBoardFromJson(json)

        completionPercent = evaluateCompletionPercent(schema)
    }

    private fun extractBoardFromJson(json: String) : List<List<Int>> =
        Gson().fromJson(json, SudokuBoard::class.java).board

    private fun compressToDBEntity() : SavedGameDBEntity
    {
        val gson = Gson()
        val dataArray = gson.toJson(SudokuBoard(this.schema, viewModel, screenWidth - margin.dp)).toByteArray()
        val myFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dateString = this.saveDate.format(myFormatter)
        return SavedGameDBEntity(this.id, this.timePassed.toInt(DurationUnit.SECONDS), this.completionPercent,
            dataArray, this.difficulty.toString(), dateString)
    }

    private fun evaluateCompletionPercent(schema: List<List<Int>>) : String
    {
        var counter = 0
        for(i in 0..8)
        {
            for(j in 0..8)
            {
                if(schema[i][j] != 0)
                {
                    counter++
                }
            }
        }

        return String.format("%.2f%%", counter/81.0f*100)
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SudokuGameModelEntryPoint {
        fun getSavedGamesDAO() : SavedGamesDAO
    }

    fun saveInDB(appContext: Context)
    {
        val dao = EntryPointAccessors.fromApplication(appContext, SudokuGameModelEntryPoint::class.java).getSavedGamesDAO()
        val dbEntity = compressToDBEntity()

        runBlocking {
            val job = CoroutineScope(Dispatchers.IO).launch {
                dao.saveGame(dbEntity)
            }

            job.join()
        }
    }

    fun deleteFromDB(appContext: Context)
    {
        if(this.id != 0)
        {
            val dbEntity = compressToDBEntity()
            val dao = EntryPointAccessors.fromApplication(appContext, SudokuGameModelEntryPoint::class.java).getSavedGamesDAO()

            runBlocking {
                val job = CoroutineScope(Dispatchers.IO).launch {

                    dao.deleteGame(dbEntity)
                }

                job.join()
            }
        }
    }
}