package com.example.sudokump.common

import android.app.Activity
import android.widget.Toast
import com.example.sudokump.model.SudokuNode
import com.example.sudokump.model.getHash
import kotlin.math.sqrt


/*
In kotlin , extensions allows the use of the OCP , that stands for open closed principle , which states
that software entities can access and use the extensions but can't modify it.
In short , we can add new functionalities without rewriting or modify the existing source code.
*/

/*
From now on we can use the function makeToast without define the entire activity , because it has been
declared below as an internal function
 */
internal fun Activity.makeToast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()

}

internal fun Long.toTime():String{
    if(this >= 3600 ) return "+59.59"
    var minutes= ((this % 3600) / 60 ).toString()
    if (minutes.length ==1 ) minutes ="0$minutes"
    var seconds = (this % 60 ).toString()
    if(seconds.length == 1) seconds = "0$seconds"
    return String.format("$minutes.$seconds")

}

internal val Int.sqrt: Int
    get() = sqrt(this.toDouble()).toInt()


fun mapToList (board: Map<Int, SudokuNode>) : List<List<Int>>{
    val list = mutableListOf<List<Int>>()
    for (i in (0..9)) {

        val list1 = mutableListOf<Int>()
        for (j in(0..9)){

            val value = if ( board[getHash(i,j)] != null) board[getHash(i,j)]?.value else 0
            if (value != null) {
                list1.add(value)
            }
        }
        list.add(list1)
    }
    return list
}