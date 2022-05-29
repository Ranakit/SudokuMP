package com.example.sudokump.model

import com.example.sudokump.persistency.entities.SavedGameDBEntity
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.seconds


class SudokuGameModel(savedGameDBEntity: SavedGameDBEntity) {
    val id = savedGameDBEntity.id
    val timePassed  = savedGameDBEntity.secondsPassed.seconds
    val completionPercent = savedGameDBEntity.completionPercent
    val schema : List<List<Int>>
    val difficulty = Difficulties.valueOf(savedGameDBEntity.difficulty) //Will be assured to be saved correctly to DB
    val saveDate : LocalDate //Will be assured to be saved correctly to DB

    init {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        saveDate = LocalDate.parse(savedGameDBEntity.savedDate, formatter)

        val gson = Gson()

        var json = String(savedGameDBEntity.savedSchema)
        json = json.substring(0, json.length -1)

        val board = gson.fromJson(json, SudokuBoard::class.java)

        schema = board.board
    }
}