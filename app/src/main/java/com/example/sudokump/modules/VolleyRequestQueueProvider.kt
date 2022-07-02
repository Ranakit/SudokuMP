package com.example.sudokump.modules

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)



/*Utilizzata per generare una coda di richieste HTTP*/
object VolleyRequestQueueProvider {

    @Provides
    @Singleton
    fun provideRequestQueue(@ApplicationContext context: Context) : RequestQueue
    {
        return Volley.newRequestQueue(context)
    }
}