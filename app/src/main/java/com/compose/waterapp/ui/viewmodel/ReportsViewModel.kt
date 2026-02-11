package com.compose.waterapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.waterapp.data.repository.WaterRepository
import com.compose.waterapp.domain.usecase.AggregateHourlyDataUseCase
import com.compose.waterapp.domain.usecase.AggregateWeeklyDataUseCase
import com.compose.waterapp.domain.usecase.GetWeekBoundsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

data class HourlyData(
    val hour: Int,
    val totalMl: Int
)

data class DailyData(
    val date: String,
    val totalMl: Int
)

@OptIn(ExperimentalCoroutinesApi::class)
class ReportsViewModel(
    private val waterRepository: WaterRepository,
    private val getWeekBoundsUseCase: GetWeekBoundsUseCase,
    private val aggregateWeeklyDataUseCase: AggregateWeeklyDataUseCase,
    private val aggregateHourlyDataUseCase: AggregateHourlyDataUseCase
) : ViewModel() {
    
    private val _selectedWeekStart = MutableStateFlow(
        getWeekBoundsUseCase.getStartOfWeek(Calendar.getInstance())
    )
    val selectedWeekStart: StateFlow<Calendar> = _selectedWeekStart.asStateFlow()
    
    val weeklyData: StateFlow<List<DailyData>> = _selectedWeekStart.flatMapLatest { weekStart ->
        val bounds = getWeekBoundsUseCase(weekStart)
        
        waterRepository.getIntakesForPeriod(bounds.start.timeInMillis, bounds.end.timeInMillis)
            .map { intakes -> aggregateWeeklyDataUseCase(intakes, weekStart) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val hourlyData: StateFlow<List<HourlyData>> = _selectedWeekStart.flatMapLatest { weekStart ->
        val bounds = getWeekBoundsUseCase(weekStart)
        
        waterRepository.getIntakesForPeriod(bounds.start.timeInMillis, bounds.end.timeInMillis)
            .map { intakes -> aggregateHourlyDataUseCase(intakes) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun previousWeek() {
        _selectedWeekStart.value = (_selectedWeekStart.value.clone() as Calendar).apply {
            add(Calendar.WEEK_OF_YEAR, -1)
        }
    }
    
    fun nextWeek() {
        _selectedWeekStart.value = (_selectedWeekStart.value.clone() as Calendar).apply {
            add(Calendar.WEEK_OF_YEAR, 1)
        }
    }
}
