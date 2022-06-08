package com.example.sudokump.screens

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sudokump.R
import com.example.sudokump.ThemeSwitcher

@Composable
fun InitialScreen(sharedPreferences: SharedPreferences, themeSwitcher: ThemeSwitcher, navController: NavHostController) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    themeSwitcher.themeSwitch()
                    val edit : SharedPreferences.Editor = sharedPreferences.edit()
                    edit.putBoolean("isDark", !(sharedPreferences.getBoolean("isDark", isSystemInDarkTheme)))
                    edit.apply()
                }) {
                if(sharedPreferences.getBoolean("isDark", isSystemInDarkTheme)){
                Image(
                    painterResource(R.drawable.ic_action_name),"content description")
                }
                else{
                    Image(
                        painterResource(R.drawable.ic_moon),"content description")
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
            title = { Text(
                text = "SudokuMP",
                color = MaterialTheme.colors.onBackground
            ) },
            backgroundColor = MaterialTheme.colors.primary
        )
        Box(
                modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Hello!",
            )
        }
    }
}

@Composable
fun NavigationComponent(sharedPreferences: SharedPreferences, themeSwitcher: ThemeSwitcher, navController: NavHostController) {
    NavHost(navController = navController, startDestination = "init") {
        composable("init") { InitialScreen(sharedPreferences ,themeSwitcher, navController)}
    }
}