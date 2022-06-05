package com.example.sudokump.model

import android.content.res.Resources
import com.example.sudokump.R

enum class Difficulties {
    EASY, MEDIUM, HARD;

    /*override fun toString(): String {
        return when(this) {
            EASY -> Resources.getSystem().getString(R.string.easy)
            MEDIUM -> Resources.getSystem().getString(R.string.medium)
            HARD -> Resources.getSystem().getString(R.string.hard)
        }
    }*/
}