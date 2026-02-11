package com.compose.waterapp.domain.usecase

import com.compose.waterapp.data.entity.WaterIntake
import com.compose.waterapp.ui.viewmodel.HourlyData
import java.util.*

class AggregateHourlyDataUseCase {
    
    operator fun invoke(intakes: List<WaterIntake>): List<HourlyData> {
        val hourlyMap = initializeHourlyMap()
        
        intakes.forEach { intake ->
            val cal = Calendar.getInstance().apply { timeInMillis = intake.timestamp }
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            hourlyMap[hour] = (hourlyMap[hour] ?: 0) + intake.volumeMl
        }
        
        return hourlyMap.map { HourlyData(it.key, it.value) }.sortedBy { it.hour }
    }
    
    private fun initializeHourlyMap(): MutableMap<Int, Int> {
        val map = mutableMapOf<Int, Int>()
        for (hour in 0..23) {
            map[hour] = 0
        }
        return map
    }
}
