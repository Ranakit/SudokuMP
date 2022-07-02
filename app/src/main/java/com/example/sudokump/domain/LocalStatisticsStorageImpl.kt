package com.example.sudokump.domain

import com.example.sudokump.model.Difficulties

class LocalStatisticsStorageImpl():IStatisticsRepository{
    override suspend fun getStatistics(
        onSuccess: (UserStatistics) -> Unit,
        onError: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateStatistic(
        time: Long,
        diff: Difficulties,
        boundary: Int,
        onSuccess: (isRecord: Boolean) -> Unit,
        onError: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

}