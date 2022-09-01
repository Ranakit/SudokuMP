package com.example.sudokump.persistency.dao

import androidx.room.*
import com.example.sudokump.persistency.entities.SavedGameDBEntity

@Dao
interface SavedGamesDAO {

    @Insert
    fun saveGame(savedGame : SavedGameDBEntity)

    @Delete
    fun deleteGame(savedGame: SavedGameDBEntity)

    @Update
    fun updateGame(savedGame: SavedGameDBEntity)

    @Query("SELECT * FROM saved_games WHERE completion_percent != '100.00%'")
    fun getSavedGames() : List<SavedGameDBEntity>

    @Query("SELECT * FROM saved_games WHERE completion_percent == '100.00%'")
    fun getCompletedGames() : List<SavedGameDBEntity>

    @Query("SELECT * FROM saved_games WHERE id == :id")
    fun getSavedGameById(id: Int) : SavedGameDBEntity

    @Query("SELECT MAX(id) FROM saved_games")
    fun getNewGameId() : Int

}