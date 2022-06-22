package com.example.sudokump.model

import androidx.compose.ui.unit.Dp
import com.example.sudokump.ui.theme.activeGame.ActiveGameEvent
import com.example.sudokump.ui.theme.activeGame.ActiveGameViewModel

data class SudokuBoard(
    val board: (ActiveGameEvent) -> Unit,
    val viewModel: ActiveGameViewModel,
    val dp: Dp
)