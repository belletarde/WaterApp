package com.compose.waterapp.data.repository

import com.compose.waterapp.data.dao.CustomCupDao
import com.compose.waterapp.data.entity.CustomCup
import kotlinx.coroutines.flow.Flow

class CustomCupRepository(private val customCupDao: CustomCupDao) {
    
    suspend fun addCustomCup(name: String, volumeMl: Int) {
        customCupDao.insert(CustomCup(name = name, volumeMl = volumeMl))
    }
    
    fun getAllCustomCups(): Flow<List<CustomCup>> {
        return customCupDao.getAllCups()
    }
    
    suspend fun deleteCustomCup(customCup: CustomCup) {
        customCupDao.delete(customCup)
    }
    
    suspend fun updateCustomCup(customCup: CustomCup) {
        customCupDao.update(customCup)
    }
}
