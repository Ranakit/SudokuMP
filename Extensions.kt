package com.fabrizio.sudoku.common

import android.app.Activity
import android.widget.Toast
import com.fabrizio.sudoku.R
import com.fabrizio.sudoku.domain.Difficulty



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

internal val Difficulty.toLocalizedResource: Int

    get(){
        return when (this){
            Difficulty.EASY -> R.string.easy
            Difficulty.MEDIUM -> R.string.medium
            Difficulty.HARD -> R.string.hard


        }
    }
