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
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Module
@InstallIn(SingletonComponent::class)
object NewSudokuGamesProvider {

    private const val url = "https://sugoku.herokuapp.com/board?difficulty="

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

        var string = "{\"board\":[[0,0,4,8,7,0,0,0,0],[0,2,0,0,0,0,7,8,0],[0,0,0,0,0,9,0,4,0],[0,1,0,0,0,0,0,0,0],[0,4,0,1,0,0,0,2,0],[7,8,9,0,6,0,0,1,4],[0,3,0,6,8,0,0,7,0],[0,5,0,9,0,4,0,0,1],[8,0,0,7,1,3,4,0,5]]}"
        try {
            string = requestFuture.get(1, TimeUnit.SECONDS)
        }catch (e: InterruptedException)
        {
            println(e.stackTrace)
        }
        catch (e: ExecutionException)
        {
            println(e.stackTrace)
        }
        catch (e: TimeoutException)
        {
            println(e.stackTrace)
        }

        return SudokuGameModel(difficulty, string)
    }
}