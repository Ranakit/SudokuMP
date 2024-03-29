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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sudokump.R
import com.example.sudokump.SudokuMP
import com.example.sudokump.viewmodel.InitialScreenViewModel

@Composable
fun InitialScreen(sharedPreferences: SharedPreferences, sudokuMP: SudokuMP, navController: NavHostController) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val viewModel = InitialScreenViewModel(LocalContext.current)
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
                    text = if(viewModel.isCompletedGames && viewModel.isSavedGames){
                        stringResource(id = R.string.welcome_back)}
                    else{
                        stringResource(id = R.string.welcome_first)},
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
                        text = stringResource(id = R.string.start_new_game),
                        style = MaterialTheme.typography.button
                    )
                }
                if (isNewGameClicked) {
                    Buttons(navController)
                }

                if (sharedPreferences.getInt("id",-1) != -1) {
                    Button(
                        onClick = {
                            navController.navigate("game/${sharedPreferences.getInt("id", -3)}")
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
                            text = stringResource(id = R.string.continue_last_game),
                            style = MaterialTheme.typography.button,
                        )
                    }
                }

                if(viewModel.isSavedGames) {
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
                            text = stringResource(id = R.string.view_saved_games),
                            style = MaterialTheme.typography.button
                        )
                    }
                }
                    if (viewModel.isCompletedGames){
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
                            text = stringResource(id = R.string.view_complete_games),
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            }
        }
    }
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
            text = stringResource(id = R.string.easy)
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
            text = stringResource(id = R.string.medium)
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
            text = stringResource(id = R.string.hard)
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