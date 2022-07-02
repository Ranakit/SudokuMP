package com.example.sudokump.domain

import com.example.sudokump.model.Difficulties

interface IStatisticsRepository {
    suspend fun getStatistics(
        onSuccess: (UserStatistics)-> Unit,
        onError : (Exception) -> Unit
    )

    suspend fun updateStatistic(
        time: Long,
        diff: Difficulties,
        boundary : Int,
        onSuccess: (isRecord: Boolean) -> Unit,   // Affects the user interface in case the user set a record in resolving a sudoku board
        onError: (Exception) -> Unit

    )
}