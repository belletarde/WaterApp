package com.compose.waterapp.domain.usecase

import com.compose.waterapp.domain.model.ActivityLevel

class CalculateDailyGoalUseCase {
    
    operator fun invoke(weightKg: Float, activityLevel: ActivityLevel): Int {
        return (weightKg * activityLevel.multiplier).toInt()
    }
}
