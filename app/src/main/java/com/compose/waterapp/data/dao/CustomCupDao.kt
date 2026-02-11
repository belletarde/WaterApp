package com.compose.waterapp.data.dao

import androidx.room.*
import com.compose.waterapp.data.entity.CustomCup
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomCupDao {
    @Insert
    suspend fun insert(customCup: CustomCup)
    
    @Query("SELECT * FROM custom_cups ORDER BY name ASC")
    fun getAllCups(): Flow<List<CustomCup>>
    
    @Delete
    suspend fun delete(customCup: CustomCup)
    
    @Update
    suspend fun update(customCup: CustomCup)
}
