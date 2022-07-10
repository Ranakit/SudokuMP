package com.example.sudokump.modules

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.concurrent.thread

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
        var baseJSON = JSONObject().put("board", defaultBoard)
        Log.e("API", baseJSON.toString())
        val jsonObjectRequest = JsonObjectRequest(
            this.url + difficulty.toString().lowercase(),
            { response ->
                val data = response.toString()
                baseJSON = JSONObject(data)
                Log.e("API2", baseJSON.toString())
            },
            {}
        )
        volleyRequestQueue.add(jsonObjectRequest)
        /*val requestFuture : RequestFuture<JSONObject> = RequestFuture.newFuture()
        val jsonObjectRequest = JsonObjectRequest(
            this.url + difficulty.toString().lowercase(),
            requestFuture,
            requestFuture
        )

        volleyRequestQueue.add(jsonObjectRequest)
        try {
            baseJSON = requestFuture.get(10, TimeUnit.SECONDS)
            Log.e("API2", baseJSON.toString())
        } catch (e: InterruptedException) {
            println(e.stackTrace)
        } catch (e: ExecutionException) {
            println(e.stackTrace)
        } catch (e: TimeoutException) {
            println(e.stackTrace)
        }*/

        return SudokuGameModel(difficulty, "{\"board\" : ${baseJSON.get("board")}}")
    }
}