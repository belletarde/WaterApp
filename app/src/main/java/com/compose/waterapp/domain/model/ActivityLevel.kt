package com.compose.waterapp.domain.model

enum class ActivityLevel(val multiplier: Float) {
    LOW(30f),
    MODERATE(35f),
    HIGH(40f);
    
    companion object {
        fun fromString(value: String): ActivityLevel {
            return when (value.lowercase()) {
                "low" -> LOW
                "moderate" -> MODERATE
                "high" -> HIGH
                else -> MODERATE
            }
        }
    }
}
