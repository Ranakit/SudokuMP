package com.example.sudokump.screens

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.outlinedButtonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sudokump.R
import com.example.sudokump.SudokuMP

@Composable
fun InitialScreen(sharedPreferences: SharedPreferences, sudokuMP: SudokuMP, navController: NavHostController) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    var isNewGameClicked by remember {
        mutableStateOf(false)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    sudokuMP.themeSwitch()
                    val edit: SharedPreferences.Editor = sharedPreferences.edit()
                    edit.putBoolean(
                        "isDark",
                        !(sharedPreferences.getBoolean("isDark", isSystemInDarkTheme))
                    )
                    edit.apply()
                }) {
                if (sharedPreferences.getBoolean("isDark", isSystemInDarkTheme)) {
                    Image(
                        painterResource(R.drawable.ic_action_name), "content description"
                    )
                } else {
                    Image(
                        painterResource(R.drawable.ic_moon), "content description"
                    )
                }
            }
        },
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.onSecondary,
                cutoutShape = MaterialTheme.shapes.small.copy(
                    CornerSize(percent = 50)
                )
            ) {
            }
        }
    ) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.onSecondary,
            title = {
                Text(
                    text = "Welcome back!",
                    color = MaterialTheme.colors.onBackground
                )
            }
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
            ) {
                Button(
                    onClick = {isNewGameClicked = !isNewGameClicked},
                    shape = MaterialTheme.shapes.medium,
                    colors = outlinedButtonColors(MaterialTheme.colors.secondary, Black),
                    modifier = Modifier
                        .height(70.dp)
                        .width(350.dp)
                        .padding(vertical = 10.dp)
                )
                {
                    Text(
                        text = "Start New Game",
                        style = MaterialTheme.typography.button
                    )
                }
                if (isNewGameClicked) {
                    Buttons()
                }
                if (isSystemInDarkTheme){
                Button(
                    onClick = { /*TODO*/ },
                    shape = MaterialTheme.shapes.medium,
                    colors = outlinedButtonColors(MaterialTheme.colors.secondary, Black),
                    modifier = Modifier
                        .height(70.dp)
                        .width(350.dp)
                        .padding(vertical = 10.dp)
                )
                {
                    Text(
                        text = "Continue Last Game",
                        style = MaterialTheme.typography.button,
                    )
                }
                Button(
                    onClick = { navController.navigate("SavedGames")  },
                    shape = MaterialTheme.shapes.medium,
                    colors = outlinedButtonColors(MaterialTheme.colors.secondary, Black),
                    modifier = Modifier
                        .height(70.dp)
                        .width(350.dp)
                        .padding(vertical = 10.dp)
                )
                {
                    Text(
                        text = "View List of Saved Games",
                        style = MaterialTheme.typography.button
                    )
                }
                Button(
                    onClick = { navController.navigate("CompletedGames") },
                    shape = MaterialTheme.shapes.medium,
                    colors = outlinedButtonColors(MaterialTheme.colors.secondary, Black),
                    modifier = Modifier
                        .height(70.dp)
                        .width(350.dp)
                        .padding(vertical = 10.dp)
                )
                {
                    Text(
                        text = "View List of Completed Games",
                        style = MaterialTheme.typography.button
                    )
                }
            }
            }
        }
    }
}


@Composable
fun Buttons() {
    Button(
        onClick = {//TODO
        },
        shape = MaterialTheme.shapes.medium,
        colors = outlinedButtonColors(MaterialTheme.colors.onError, Black),
        modifier = Modifier
            .height(50.dp)
            .width(250.dp)
            .padding(vertical = 5.dp, horizontal = 30.dp)
    )
    {
        Text(
            text = "Easy"
        )
    }
    Button(
        onClick = {//TODO
             },
        shape = MaterialTheme.shapes.medium,
        colors = outlinedButtonColors(MaterialTheme.colors.error, Black),
        modifier = Modifier
            .height(50.dp)
            .width(250.dp)
            .padding(vertical = 5.dp, horizontal = 30.dp)
    )
    {
        Text(
            text = "Medium"
        )
    }
    Button(
        onClick = {
            //TODO
        },
        shape = MaterialTheme.shapes.medium,
        colors = outlinedButtonColors(MaterialTheme.colors.secondaryVariant, Black),
        modifier = Modifier
            .height(50.dp)
            .width(250.dp)
            .padding(vertical = 5.dp, horizontal = 30.dp)
    )
    {
        Text(
            text = "Hard"
        )
    }
}


@Composable
fun NavigationComponent(sharedPreferences: SharedPreferences, sudokuMP: SudokuMP, navController: NavHostController) {
    NavHost(navController = navController, startDestination = "init") {
        composable("init") { InitialScreen(sharedPreferences ,sudokuMP, navController)}
        composable("SavedGames"){SavedGamesScreen(
            viewModel = hiltViewModel()
            )}
        composable("CompletedGames"){
            CompletedGamesScreen(
                viewModel = hiltViewModel()
            )
        }
    }
}