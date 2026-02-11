package com.compose.waterapp.domain.usecase

import java.util.*

data class WeekBounds(val start: Calendar, val end: Calendar)

class GetWeekBoundsUseCase {
    
    operator fun invoke(calendar: Calendar): WeekBounds {
        val start = getStartOfWeek(calendar)
        val end = getEndOfWeek(start)
        return WeekBounds(start, end)
    }
    
    fun getStartOfWeek(calendar: Calendar): Calendar {
        val cal = calendar.clone() as Calendar
        cal.firstDayOfWeek = Calendar.MONDAY
        
        val currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        
        val daysToSubtract = when (currentDayOfWeek) {
            Calendar.SUNDAY -> 6
            else -> currentDayOfWeek - Calendar.MONDAY
        }
        
        cal.add(Calendar.DAY_OF_YEAR, -daysToSubtract)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal
    }
    
    private fun getEndOfWeek(startOfWeek: Calendar): Calendar {
        val cal = startOfWeek.clone() as Calendar
        cal.add(Calendar.DAY_OF_YEAR, 6)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal
    }
}
