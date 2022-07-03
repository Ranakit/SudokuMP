package com.example.sudokump.modules

import com.example.sudokump.viewmodel.ActiveGameViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ActiveGameViewModelAssistedFactory {

    fun create(gameId : Int) : ActiveGameViewModel
}