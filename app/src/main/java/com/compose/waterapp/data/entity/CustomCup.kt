package com.compose.waterapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_cups")
data class CustomCup(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val volumeMl: Int
)
