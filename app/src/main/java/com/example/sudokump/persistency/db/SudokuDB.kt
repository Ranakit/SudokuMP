package com.example.sudokump.persistency.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sudokump.persistency.dao.SavedGamesDAO
import com.example.sudokump.persistency.entities.SavedGameDBEntity

@Database(entities = [SavedGameDBEntity::class], version = 1)
abstract class SudokuDB: RoomDatabase() {

    abstract fun getSavedGamesDAO() : SavedGamesDAO
}