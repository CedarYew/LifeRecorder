package com.example.test.data.repository

import com.example.test.data.dao.DailyDataDao
import com.example.test.data.model.DailyData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DailyDataRepository(private val dailyDataDao: DailyDataDao) {
    fun getDailyDataByDate(date: Int): Flow<DailyData> {
        return dailyDataDao.getDailyDataByDate(date).map { it ?: DailyData(date = date) }
    }

    suspend fun insertOrUpdate(dailyData: DailyData) {
        dailyDataDao.insert(dailyData)
    }

    suspend fun delete(dailyData: DailyData) {
        dailyDataDao.delete(dailyData)
    }

    fun getAllDailyData(): Flow<List<DailyData>> {
        return dailyDataDao.getAllDailyData()
    }
} 