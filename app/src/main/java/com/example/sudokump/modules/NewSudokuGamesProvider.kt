package com.example.sudokump.modules

import android.content.Context
import android.widget.Toast
import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.model.SudokuGrid
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


@Module
@InstallIn(SingletonComponent::class)
object NewSudokuGamesProvider {
    private const val url = "https://sugoku.herokuapp.com/"

    @EasySudokuGame
    @Provides
    fun provideNewEasySudokuGame(@ApplicationContext context: Context) : SudokuGameModel
    {
        return getSudokuGame(Difficulties.EASY, context)
    }

    @MediumSudokuGame
    @Provides
    fun provideNewMediumSudokuGame(@ApplicationContext context: Context) : SudokuGameModel
    {
        return getSudokuGame(Difficulties.MEDIUM, context)
    }

    @HardSudokuGame
    @Provides
    fun provideNewHardSudokuGame(@ApplicationContext context: Context) : SudokuGameModel
    {
        return getSudokuGame(Difficulties.HARD, context)
    }

    private fun getSudokuGame(difficulty: Difficulties, context: Context) : SudokuGameModel
    {
        try {
            var sudokuGrid : SudokuGrid? = null
            val job = CoroutineScope(Dispatchers.IO).launch {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val sudokuAPI = retrofit.create(SudokuAPI::class.java)
                val jsonCALL = sudokuAPI.sudokuGrid(difficulty.toString())
                sudokuGrid = jsonCALL.execute().body()!!
            }
            runBlocking {
                job.join()
            }
            return SudokuGameModel(difficulty, sudokuGrid!!)
        }
        catch (e : IOException) {
            Toast.makeText(context, "internet connection failed, running a demo", Toast.LENGTH_SHORT).show()
            val savedSchema =
                "{\"board\":[[0,0,4,8,7,0,0,0,0],[0,2,0,0,0,0,7,8,0],[0,0,0,0,0,9,0,4,0],[0,1,0,0,0,0,0,0,0],[0,4,0,1,0,0,0,2,0],[7,8,9,0,6,0,0,1,4],[0,3,0,6,8,0,0,7,0],[0,5,0,9,0,4,0,0,1],[8,0,0,7,1,3,4,0,5]]}"
            return SudokuGameModel(
                Difficulties.EASY,
                Gson().fromJson(savedSchema, SudokuGrid::class.java)
            )
        }
    }
}