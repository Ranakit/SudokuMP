package com.example.sudokump

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sudokump.modules.NewSudokuGamesProvider
import org.json.JSONObject
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class VolleyRequestFutureTest {

    private val url = "https://sugoku.herokuapp.com/board?difficulty=easy"

    @Test
    fun volleyRequestFutureTest()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val volleyRequestQueue = Volley.newRequestQueue(appContext)

        val requestFuture : RequestFuture<JSONObject> = RequestFuture.newFuture()
        val stringRequest = JsonObjectRequest(url, requestFuture, requestFuture)

        volleyRequestQueue.add(stringRequest)

        val oriObject = JSONObject()
        var jsonObject = oriObject
        try {
            jsonObject = requestFuture.get(10, TimeUnit.SECONDS)
            println(jsonObject.toString())
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

        assertNotEquals(oriObject,jsonObject)
    }

}