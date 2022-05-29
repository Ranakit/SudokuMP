package com.example.sudokump.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.mutableStateOf
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class SavedGameViewModel : Parcelable {

    @IgnoredOnParcel
    private val _id = mutableStateOf(0)
    var id : Int
    get() = _id.value
    set(value) {_id.value = value}

    @IgnoredOnParcel
    private val _timePassed = mutableStateOf(0)
    var timePassed : Int
        get() = _timePassed.value
        set(value) {_timePassed.value = value}
}