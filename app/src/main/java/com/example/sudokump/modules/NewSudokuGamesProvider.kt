package com.example.sudokump.modules

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NewSudokuGamesProvider {
    private const val url = "https://sugoku.herokuapp.com/"

    @EasySudokuGame
    @Provides
    fun provideNewEasySudokuGame() : SudokuGameModel
    {
        return getSudokuGame(Difficulties.EASY)
    }

    @MediumSudokuGame
    @Provides
    fun provideNewMediumSudokuGame() : SudokuGameModel
    {
        return getSudokuGame(Difficulties.MEDIUM)
    }

    @HardSudokuGame
    @Provides
    fun provideNewHardSudokuGame() : SudokuGameModel
    {
        return getSudokuGame(Difficulties.HARD)
    }

    private fun getSudokuGame(difficulty: Difficulties) : SudokuGameModel
    {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val sudokuAPI = retrofit.create(SudokuAPI::class.java)
        val jsonCALL = sudokuAPI.sudokuGrid(difficulty.toString())
        val sudokuGrid = jsonCALL.execute().body()!!


        return SudokuGameModel(difficulty, sudokuGrid)
    }
}