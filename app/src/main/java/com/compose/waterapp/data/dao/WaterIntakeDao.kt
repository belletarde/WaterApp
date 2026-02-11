package com.compose.waterapp.data.dao

import androidx.room.*
import com.compose.waterapp.data.entity.WaterIntake
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    @Insert
    suspend fun insert(waterIntake: WaterIntake)
    
    @Query("SELECT * FROM water_intake WHERE timestamp >= :startOfDay AND timestamp < :endOfDay ORDER BY timestamp DESC")
    fun getIntakesForDay(startOfDay: Long, endOfDay: Long): Flow<List<WaterIntake>>
    
    @Query("SELECT * FROM water_intake WHERE timestamp >= :startTime AND timestamp < :endTime ORDER BY timestamp ASC")
    fun getIntakesForPeriod(startTime: Long, endTime: Long): Flow<List<WaterIntake>>
    
    @Query("SELECT SUM(volumeMl) FROM water_intake WHERE timestamp >= :startOfDay AND timestamp < :endOfDay")
    fun getTotalForDay(startOfDay: Long, endOfDay: Long): Flow<Int?>
    
    @Delete
    suspend fun delete(waterIntake: WaterIntake)
}
