package com.compose.waterapp.data.repository

import com.compose.waterapp.data.dao.WaterIntakeDao
import com.compose.waterapp.data.entity.WaterIntake
import kotlinx.coroutines.flow.Flow

class WaterRepository(private val waterIntakeDao: WaterIntakeDao) {
    
    suspend fun addWaterIntake(volumeMl: Int, timestamp: Long, cupName: String) {
        waterIntakeDao.insert(
            WaterIntake(
                volumeMl = volumeMl,
                timestamp = timestamp,
                cupName = cupName
            )
        )
    }
    
    fun getIntakesForDay(startOfDay: Long, endOfDay: Long): Flow<List<WaterIntake>> {
        return waterIntakeDao.getIntakesForDay(startOfDay, endOfDay)
    }
    
    fun getIntakesForPeriod(startTime: Long, endTime: Long): Flow<List<WaterIntake>> {
        return waterIntakeDao.getIntakesForPeriod(startTime, endTime)
    }
    
    fun getTotalForDay(startOfDay: Long, endOfDay: Long): Flow<Int?> {
        return waterIntakeDao.getTotalForDay(startOfDay, endOfDay)
    }
    
    suspend fun deleteIntake(waterIntake: WaterIntake) {
        waterIntakeDao.delete(waterIntake)
    }
}
