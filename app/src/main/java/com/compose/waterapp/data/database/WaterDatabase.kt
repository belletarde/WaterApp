package com.compose.waterapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.compose.waterapp.data.dao.CustomCupDao
import com.compose.waterapp.data.dao.WaterIntakeDao
import com.compose.waterapp.data.entity.CustomCup
import com.compose.waterapp.data.entity.WaterIntake

@Database(
    entities = [WaterIntake::class, CustomCup::class],
    version = 1,
    exportSchema = false
)
abstract class WaterDatabase : RoomDatabase() {
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun customCupDao(): CustomCupDao
}
