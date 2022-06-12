package com.example.sudokump.di

import android.content.Context
import com.example.sudokump.SudokuMP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @ActivityScoped
    @Provides
    fun provideApplication(@ApplicationContext app: Context) : SudokuMP{
        return app as SudokuMP
    }
}