package com.example.sudokump.common

import kotlin.coroutines.CoroutineContext


/*
 REMEMBER: COROUTINES ARE DIFFERENT FROM THREADS.
 With the dispatcher implemented below we can tell to the coroutines which threads to run on

 */
interface DispatcherProvider {
    fun provideUIContext() : CoroutineContext
    fun provideIOContext() : CoroutineContext

}