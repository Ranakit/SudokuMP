package com.example.sudokump.common

import kotlinx.coroutines.*

abstract class AsyncReturnTask<T> {

    private var job : Job? = null
    private var retVal : T? = null

    protected abstract fun taskToExecuteWithReturn() : T

    fun executeAsyncReturnTask()
    {
        job = CoroutineScope(Dispatchers.IO).launch {
            retVal = taskToExecuteWithReturn()
        }
    }

    fun joinTask() : T
    {
        if (job != null)
        {
            runBlocking {
                job!!.join()
            }
        }

        if (retVal == null) {
            throw NoReturnException()
        }

        return retVal!!
    }
}

class NoReturnException : Exception()