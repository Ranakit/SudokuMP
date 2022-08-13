package com.example.sudokump.model

import android.content.Context
import com.example.sudokump.common.mapToList
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
    var completionPercent : String
    val schema : SudokuSchema
    val difficulty : Difficulties
    val saveDate : LocalDate

    constructor(savedGameDBEntity: SavedGameDBEntity) {
        id = savedGameDBEntity.id
        timePassed  = savedGameDBEntity.secondsPassed.seconds
        completionPercent = savedGameDBEntity.completionPercent
        difficulty = Difficulties.valueOf(savedGameDBEntity.difficulty)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        saveDate = LocalDate.parse(savedGameDBEntity.savedDate, formatter)
        var gridJson = String(savedGameDBEntity.savedSchema)
        gridJson = gridJson.substring(0, gridJson.length -1)
        var readOnlyJson = String(savedGameDBEntity.readOnlyTiles)
        readOnlyJson = readOnlyJson.substring(0, readOnlyJson.length -1)
        schema = SudokuSchema(extractGridFromJson(gridJson), extractReadOnlyTilesFromJson(readOnlyJson))
    }

    constructor(gameDifficulty: Difficulties, sudokuGrid: SudokuGrid)
    {
        id = 0
        timePassed = Duration.ZERO
        difficulty = gameDifficulty
        saveDate = LocalDate.now()
        schema = SudokuSchema(sudokuGrid)
        completionPercent = schema.evaluateCompletionPercent()
    }

    constructor(
        elapsedTime: Long,
        id:Int,
        board: HashMap<Int, SudokuNode>,
        difficulty: Difficulties){
        this.id = id
        this.timePassed = elapsedTime.seconds
        this.difficulty= difficulty
        this.schema = SudokuSchema(SudokuGrid(mutableListOf(mutableListOf())))
        completionPercent = schema.evaluateCompletionPercent()
        this.saveDate = LocalDate.now()


    }

    private fun extractGridFromJson(json : String) : SudokuGrid
    {
        return Gson().fromJson(json, SudokuGrid::class.java)
    }

    private fun extractReadOnlyTilesFromJson(json : String) : SudokuTileSet
    {
        return Gson().fromJson(json, SudokuTileSet::class.java)
    }

    private fun compressToDBEntity() : SavedGameDBEntity
    {
        val gson = Gson()
        val dataArray = gson.toJson(SudokuGrid(mapToList(this.schema.map))).toByteArray()
        val readOnlyTilesArray = gson.toJson(schema.getReadOnlyTiles()).toByteArray()
        val myFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dateString = this.saveDate.format(myFormatter)
        return SavedGameDBEntity(this.id, this.timePassed.toInt(DurationUnit.SECONDS), this.completionPercent,
            dataArray, this.difficulty.toString(), dateString, readOnlyTilesArray)
    }

    fun evaluateCompletionPercent() : String
    {
        this.completionPercent = this.schema.evaluateCompletionPercent()
        return this.completionPercent
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

    companion object {
        fun getFromDB(appContext: Context, id : Int) : SudokuGameModel
        {
            val dao = EntryPointAccessors.fromApplication(appContext, SudokuGameModelEntryPoint::class.java).getSavedGamesDAO()
            val dbEntity = dao.getLastSavedGame(id)
            return SudokuGameModel(dbEntity)
        }
    }
}