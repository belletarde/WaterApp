package com.compose.waterapp.domain.usecase

import com.compose.waterapp.data.entity.WaterIntake
import com.compose.waterapp.ui.viewmodel.DailyData
import java.util.*

class AggregateWeeklyDataUseCase {
    
    operator fun invoke(intakes: List<WaterIntake>, weekStart: Calendar): List<DailyData> {
        val dailyMap = initializeDailyMap(weekStart)
        
        intakes.forEach { intake ->
            val cal = Calendar.getInstance().apply { timeInMillis = intake.timestamp }
            val dateKey = formatDateKey(cal)
            if (dailyMap.containsKey(dateKey)) {
                dailyMap[dateKey] = (dailyMap[dateKey] ?: 0) + intake.volumeMl
            }
        }
        
        return buildDailyDataList(weekStart, dailyMap)
    }
    
    private fun initializeDailyMap(weekStart: Calendar): MutableMap<String, Int> {
        val map = mutableMapOf<String, Int>()
        for (i in 0..6) {
            val date = (weekStart.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, i)
            }
            map[formatDateKey(date)] = 0
        }
        return map
    }
    
    private fun buildDailyDataList(weekStart: Calendar, dailyMap: Map<String, Int>): List<DailyData> {
        val result = mutableListOf<DailyData>()
        for (i in 0..6) {
            val date = (weekStart.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, i)
            }
            val dateKey = formatDateKey(date)
            result.add(DailyData(dateKey, dailyMap[dateKey] ?: 0))
        }
        return result
    }
    
    private fun formatDateKey(calendar: Calendar): String {
        return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}"
    }
}
