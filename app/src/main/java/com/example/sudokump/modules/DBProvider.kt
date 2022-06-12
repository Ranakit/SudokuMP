package com.example.sudokump.modules

import android.content.Context
import androidx.room.Room
import com.example.sudokump.persistency.dao.SavedGamesDAO
import com.example.sudokump.persistency.db.SudokuDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBProvider {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): SudokuDB {

        return Room.databaseBuilder(
            context,
            SudokuDB::class.java,
            "SavedGamesDB"
        ).createFromAsset("SavedGamesDB.db").build()
    }

    @Provides
    @Singleton
    fun provideSavedGamesDAO(db: SudokuDB) : SavedGamesDAO
    {
        return db.getSavedGamesDAO()
    }
}