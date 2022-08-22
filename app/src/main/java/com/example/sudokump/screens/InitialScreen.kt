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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sudokump.MainActivity
import com.example.sudokump.R
import com.example.sudokump.SudokuMP
import com.example.sudokump.persistency.dao.SavedGamesDAO
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun InitialScreen(sharedPreferences: SharedPreferences, sudokuMP: SudokuMP, navController: NavHostController) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val dao = EntryPointAccessors.fromApplication(LocalContext.current, MainActivity.MainActivityEntryPoint::class.java).getSavedGamesDAO()
    val isSavedGames = isSavedGames(dao)
    val isCompletedGames = isCompletedGames(dao)
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
                backgroundColor = MaterialTheme.colors.primary,
                cutoutShape = MaterialTheme.shapes.small.copy(
                    CornerSize(percent = 50)
                )
            ) {
            }
        }
    ) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            title = {
                Text(
                    text = if(isCompletedGames && isSavedGames){"Welcome back!"}
                    else{"Welcome to SudokuMP!"},
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
                    Buttons(navController)
                }

                if (isSavedGames) {
                    Button(
                        onClick = {
                            navController.navigate("game/-3")
                        },
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
                        onClick = { navController.navigate("SavedGames") },
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
                }
                if (isCompletedGames){
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

fun isSavedGames(dao: SavedGamesDAO): Boolean {
    var isSavedGames = false
    val job = CoroutineScope(Dispatchers.IO).launch {
        isSavedGames = dao.getSavedGames().isNotEmpty()
    }
    runBlocking {
        job.join()
    }
    return isSavedGames
}

fun isCompletedGames(dao: SavedGamesDAO): Boolean {
    var isCompletedGames = false
    val job = CoroutineScope(Dispatchers.IO).launch {
        isCompletedGames = dao.getCompletedGames().isNotEmpty()
    }
    runBlocking {
        job.join()
    }
    return isCompletedGames
}


@Composable
fun Buttons(navigator : NavController) {
    Button(
        onClick = {
            navigator.navigate("game/-2")
        },
        shape = MaterialTheme.shapes.medium,
        colors = outlinedButtonColors(MaterialTheme.colors.onError, Black),
        modifier = Modifier
            .height(50.dp)
            .width(350.dp)
            .padding(vertical = 5.dp, horizontal = 80.dp)
    )
    {
        Text(
            text = "Easy"
        )
    }
    Button(
        onClick = {
            navigator.navigate("game/-1")
             },
        shape = MaterialTheme.shapes.medium,
        colors = outlinedButtonColors(MaterialTheme.colors.error, Black),
        modifier = Modifier
            .height(50.dp)
            .width(350.dp)
            .padding(vertical = 5.dp, horizontal = 80.dp)
    )
    {
        Text(
            text = "Medium"
        )
    }
    Button(
        onClick = {
            navigator.navigate("game/0")
        },
        shape = MaterialTheme.shapes.medium,
        colors = outlinedButtonColors(MaterialTheme.colors.secondaryVariant, Black),
        modifier = Modifier
            .height(50.dp)
            .width(350.dp)
            .padding(vertical = 5.dp, horizontal = 80.dp)
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
            viewModel = hiltViewModel(), navController
            )}
        composable("CompletedGames"){
            CompletedGamesScreen(
            viewModel = hiltViewModel()
            )
        }
        composable("Game/{gameId}",
            arguments = listOf(navArgument("gameId") {
                type = NavType.IntType
            })){ navBackStackEntry ->
                val arguments = navBackStackEntry.arguments
                if(arguments != null)
                {
                    val id = arguments.getInt("gameId", -2)
                    ActiveGameScreen(id, navController)}
                }

    }
}