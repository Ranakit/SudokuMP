package com.example.sudokump.modules

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NewSudokuGamesProvider {

    private val url = "https://sugoku.herokuapp.com/board?difficulty="

    @EasySudokuGame
    @Provides
    fun provideNewEasySudokuGame(volleyRequestQueue: RequestQueue) : SudokuGameModel
    {
        return getSudokuGame(volleyRequestQueue, Difficulties.EASY)
    }

    @MediumSudokuGame
    @Provides
    fun provideNewMediumSudokuGame(volleyRequestQueue: RequestQueue) : SudokuGameModel
    {
        return getSudokuGame(volleyRequestQueue, Difficulties.MEDIUM)
    }

    @HardSudokuGame
    @Provides
    fun provideNewHardSudokuGame(volleyRequestQueue: RequestQueue) : SudokuGameModel
    {
        return getSudokuGame(volleyRequestQueue, Difficulties.HARD)
    }

    private fun getSudokuGame(volleyRequestQueue: RequestQueue, difficulty: Difficulties) : SudokuGameModel
    {
        val requestFuture : RequestFuture<String> = RequestFuture.newFuture()
        val stringRequest = StringRequest(
            Request.Method.GET,
            url + difficulty.toString().lowercase(),
            requestFuture,
            requestFuture
        )

        volleyRequestQueue.add(stringRequest)

        return SudokuGameModel(difficulty, requestFuture.get())
    }
}