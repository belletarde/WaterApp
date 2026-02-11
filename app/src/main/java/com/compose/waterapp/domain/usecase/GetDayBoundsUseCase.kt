package com.compose.waterapp.domain.usecase

import java.util.*

data class DayBounds(val start: Long, val end: Long)

class GetDayBoundsUseCase {
    
    operator fun invoke(calendar: Calendar): DayBounds {
        val start = getStartOfDay(calendar)
        val end = getEndOfDay(calendar)
        return DayBounds(start, end)
    }
    
    private fun getStartOfDay(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
    
    private fun getEndOfDay(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
}
