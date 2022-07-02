package com.example.sudokump.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.sudokump.SudokuMP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @ActivityScoped
    @Provides
    fun provideApplication(@ApplicationContext app: Context) : SudokuMP{
        return app as SudokuMP
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule{
    @Provides
    fun provideSharedPreferences(sudokuMP: SudokuMP): SharedPreferences{
        return sudokuMP.getSharedPreferences("sharedPreferences", MODE_PRIVATE)
    }
}