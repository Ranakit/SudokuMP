package com.example.sudokump.screens

import androidx.compose.material.IconButton
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun InitialScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {name = ", you pressed a button"}) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.body1
                )
            }
        },
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar(
                cutoutShape = MaterialTheme.shapes.small.copy(
                    CornerSize(percent = 50)
                )
            ) {
            }
        }
    ) {
        TopAppBar(
            title = { Text(text = "SudokuMP") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.navigate("settings")
                              },
                ){Text(
                    text = "settings",
                    style = MaterialTheme.typography.button
                )}
            }
        )
        Box(
                modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Hello $name!",
            )
        }
    }
}

@Composable
fun NavigationComponent(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "init") {
        composable("init") { InitialScreen(navController)}
        composable("settings") { Settings(navController) }
    }
}


@Composable
fun Settings(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Button(
            onClick = {
                navController.navigate("init")
                      },
            modifier = Modifier
                .align(Alignment.Center)
                .size(200.dp, 200.dp),
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary, Color.White)
        ) {
            Text(
                text = "go back",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(all = 4.dp)
            )
        }
    }
}
