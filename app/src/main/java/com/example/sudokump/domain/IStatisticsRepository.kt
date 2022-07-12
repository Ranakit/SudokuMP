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
        onSuccess: (isRecord: Boolean) -> Unit,
        onError: (Exception) -> Unit

    )
}