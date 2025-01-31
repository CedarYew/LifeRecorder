package com.example.test.data.dao

import androidx.room.*
import com.example.test.data.model.DailyData
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyDataDao {
    @Query("SELECT * FROM daily_data WHERE date = :date")
    fun getDailyDataByDate(date: Int): Flow<DailyData?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dailyData: DailyData)

    @Update
    suspend fun update(dailyData: DailyData)

    @Delete
    suspend fun delete(dailyData: DailyData)

    @Query("SELECT * FROM daily_data ORDER BY date DESC")
    fun getAllDailyData(): Flow<List<DailyData>>
} 