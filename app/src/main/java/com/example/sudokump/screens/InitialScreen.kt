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
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sudokump.R
import com.example.sudokump.ThemeSwitcher

@Composable
fun InitialScreen(sharedPreferences: SharedPreferences, themeSwitcher: ThemeSwitcher, navController: NavHostController) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    var isNewGameClicked by remember {
        mutableStateOf(false)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    themeSwitcher.themeSwitch()
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
            title = {
                Text(
                    text = "Welcome back!",
                    color = MaterialTheme.colors.onBackground
                )
            },
            backgroundColor = MaterialTheme.colors.primary
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
                if (isNewGameClicked){
                    Button(
                        onClick = {//TODO
                            },
                        shape = MaterialTheme.shapes.medium,
                        colors = outlinedButtonColors(Green, Black),
                        modifier = Modifier
                            .height(50.dp)
                            .width(250.dp)
                            .padding(vertical = 5.dp, horizontal = 30.dp)
                    )
                    {
                        Text(
                            text = "Easy",
                            style = MaterialTheme.typography.h1
                        )
                    }
                    Button(
                        onClick = {isNewGameClicked = true},
                        shape = MaterialTheme.shapes.medium,
                        colors = outlinedButtonColors(Yellow, Black),
                        modifier = Modifier
                            .height(50.dp)
                            .width(250.dp)
                            .padding(vertical = 5.dp, horizontal = 30.dp)
                    )
                    {
                        Text(
                            text = "Medium",
                            style = MaterialTheme.typography.h1
                        )
                    }
                    Button(
                        onClick = {isNewGameClicked = true},
                        shape = MaterialTheme.shapes.medium,
                        colors = outlinedButtonColors(Red, Black),
                        modifier = Modifier
                            .height(50.dp)
                            .width(250.dp)
                            .padding(vertical = 5.dp, horizontal = 30.dp)
                    )
                    {
                        Text(
                            text = "Hard",
                            style = MaterialTheme.typography.h1
                        )
                    }
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
                        text = "View List of Saved Games",
                        style = MaterialTheme.typography.button
                    )
                }
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
fun NavigationComponent(sharedPreferences: SharedPreferences, themeSwitcher: ThemeSwitcher, navController: NavHostController) {
    NavHost(navController = navController, startDestination = "init") {
        composable("init") { InitialScreen(sharedPreferences ,themeSwitcher, navController)}
    }
}