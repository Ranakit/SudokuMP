package com.example.sudokump.ui.theme.activeGame


/*
With this class we can represent the user's interaction with given features
 */
sealed class ActiveGameEvent{

    data class OnInput(val input: Int) : ActiveGameEvent()
    data class OnTileFocused( val x: Int , val y: Int ): ActiveGameEvent()
    object OnNewGameClicked : ActiveGameEvent()
    object OnStart: ActiveGameEvent()
    object OnStop: ActiveGameEvent()


}
