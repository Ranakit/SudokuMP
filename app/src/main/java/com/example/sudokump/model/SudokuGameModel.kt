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
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class SudokuGameModel {
    val id : Int
    var timePassed  : Duration
    var completionPercent : String
    val schema : SudokuSchema
    val difficulty : Difficulties
    val saveDate : LocalDate

    constructor(savedGameDBEntity: SavedGameDBEntity) {
        id = savedGameDBEntity.id
        timePassed  = savedGameDBEntity.secondsPassed.seconds
        completionPercent = savedGameDBEntity.completionPercent
        difficulty = Difficulties.valueOf(savedGameDBEntity.difficulty.uppercase())
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        saveDate = LocalDate.parse(savedGameDBEntity.savedDate, formatter)
        var gridJson = String(savedGameDBEntity.savedSchema)
        gridJson = gridJson.substring(0, gridJson.length -1)
        schema = SudokuSchema(extractSavedSchemaFromJson(gridJson))
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

    private fun extractSavedSchemaFromJson(json : String) : SudokuSavedSchema
    {
        return Gson().fromJson(json, SudokuSavedSchema::class.java)
    }

    private fun compressToDBEntity() : SavedGameDBEntity
    {
        val gson = Gson()
        val schemaArray = (gson.toJson(this.schema.generateSchema())+"}").toByteArray()
        val myFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dateString = this.saveDate.format(myFormatter)
        return SavedGameDBEntity(this.id, this.timePassed.toInt(DurationUnit.SECONDS), this.completionPercent,
            schemaArray, this.difficulty.toString(), dateString)
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