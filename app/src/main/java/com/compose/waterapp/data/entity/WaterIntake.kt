package com.compose.waterapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake")
data class WaterIntake(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val volumeMl: Int,
    val timestamp: Long,
    val cupName: String
)
