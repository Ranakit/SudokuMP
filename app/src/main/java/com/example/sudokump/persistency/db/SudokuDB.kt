package com.example.sudokump.persistency.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sudokump.persistency.dao.SavedGamesDAO
import com.example.sudokump.persistency.entities.SavedGameDBEntity

@Database(entities = [SavedGameDBEntity::class], version = 1)
abstract class SudokuDB(): RoomDatabase() {

    companion object {
        private var db : SudokuDB? = null

        fun getInstance(context: Context) : SudokuDB {
            if(db == null)
            {
                db = Room.databaseBuilder(
                    context,
                    SudokuDB::class.java,
                    "SavedGamesDB").createFromAsset("SavedGamesDB.db").build()
            }

            return db as SudokuDB
        }
    }

    abstract fun getSavedGamesDAO() : SavedGamesDAO
}