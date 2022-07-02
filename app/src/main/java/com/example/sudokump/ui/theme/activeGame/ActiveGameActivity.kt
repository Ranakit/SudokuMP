package com.example.sudokump.ui.theme.activeGame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.sudokump.R
import com.example.sudokump.ui.theme.SudokuMPTheme
import com.example.sudokump.common.makeToast
import com.example.sudokump.ui.theme.activeGame.buildlogic.buildActiveGameLogic

class ActiveGameActivity : AppCompatActivity() , ActiveGameContainer {

    private lateinit var logic : ActiveGameLogic


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ActiveGameViewModel()

        /*
        Now we're going to anchor the next composable we will implement to this activity
         */

        setContent{

            SudokuMPTheme(true) {
                ActiveGameScreen(
                    /*
                    We have to pass the function that's the handler of the event that will occur
                    in the composable of the our logic model
                     */

                    onEventHandler = logic:: onEvent,
                    viewModel = viewModel

                )
            }

        }
        logic = buildActiveGameLogic(this , viewModel, applicationContext)
    }

    override fun onStart() {
        super.onStart()
        logic.onEvent(ActiveGameEvent.OnStart)
    }

    override fun onStop(){
        super.onStop()
        logic.onEvent(ActiveGameEvent.OnStop)
    }


    override fun showErr() = makeToast(getString(R.string.generic_error))

    override fun onNewGameClick() {
       startActivity(
           Intent(
               this,
               NewGameActivity:: class.java

           )
       )
    }
}