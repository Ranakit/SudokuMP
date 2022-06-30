package com.example.sudokump.persistency

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import com.example.sudokump.domain.Difficulty
import com.example.sudokump.domain.GameStorageResult
import com.example.sudokump.domain.IGameRepository
import com.example.sudokump.domain.Settings
import com.example.sudokump.model.Difficulties
import com.example.sudokump.model.SudokuGameModel
import com.example.sudokump.persistency.dao.SavedGamesDAO
import com.example.sudokump.ui.theme.activeGame.SudokuTile
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Job


class GameRepoImplementation: IGameRepository {
    override suspend fun saveGame(
            elapsedTime: Long,
            id:Int,
            board: Map<Int , SudokuTile>,
            difficulty: Difficulties,
            onSuccess: (Unit)-> Unit,
            onError: (Exception) -> Unit,
            context: Context



    ) {

        SudokuGameModel(elapsedTime , id , board , difficulty).saveInDB(context)

        val sharedPreferences = EntryPointAccessors.fromApplication(context , GameRepoEntry::class.java).getSharedPreferences()
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        edit.putInt(
            "id" ,
            id
        )
        edit.apply()

        onSuccess.invoke(
            Unit
        )



    }

    override suspend fun updateGame(onSuccess: (Unit) -> Unit, onError: (Exception) -> Unit) {

    }

    override suspend fun updateNode(
        x: Int,
        y: Int,
        color: Int,
        elapsedTime: Long,
        onSuccess: (isComplete: Boolean) -> Unit,
        onError: (Unit) -> Unit
    ) {

    }

    override suspend fun getCurrentGame(
        onSuccess: (Any, Any) -> Job,
        onError: (Exception) -> Unit,
        context: Context,


    ) {
        val sharedPreferences = EntryPointAccessors.fromApplication(context , GameRepoEntry::class.java).getSharedPreferences()
        val id = sharedPreferences.getInt("id" , -1)

        if (id == 0 )
            else if (id < 0 ) onError.invoke(Exception("wrong Id"))
        else {

            val DAO = EntryPointAccessors.fromApplication(context , GameRepoEntry::class.java).getGamesDao()
            onSuccess.invoke(DAO.getLastSavedGame(id) , false)

        }





    }

    override suspend fun updateSettings(
        settings: Settings,
        onSuccess: (Unit) -> Unit,
        onError: (Exception) -> Unit
    ) {

    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GameRepoEntry{
        fun getSharedPreferences(): SharedPreferences
        fun getGamesDao(): SavedGamesDAO

    }
}