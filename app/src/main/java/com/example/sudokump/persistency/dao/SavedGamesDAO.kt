package com.example.sudokump.persistency.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sudokump.persistency.entities.SavedGameDBEntity

@Dao
interface SavedGamesDAO {

    @Insert
    fun saveGame(savedGame : SavedGameDBEntity)

    @Delete
    fun deleteGame(savedGame: SavedGameDBEntity)

    @Query("SELECT * FROM saved_games")
    fun getSavedGames() : List<SavedGameDBEntity>
}