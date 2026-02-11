package com.compose.waterapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.waterapp.data.entity.CustomCup
import com.compose.waterapp.data.entity.WaterIntake
import com.compose.waterapp.data.repository.CustomCupRepository
import com.compose.waterapp.data.repository.PreferencesRepository
import com.compose.waterapp.data.repository.WaterRepository
import com.compose.waterapp.domain.usecase.GetDayBoundsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val waterRepository: WaterRepository,
    private val customCupRepository: CustomCupRepository,
    private val preferencesRepository: PreferencesRepository,
    private val getDayBoundsUseCase: GetDayBoundsUseCase
) : ViewModel() {
    
    val dailyGoal: StateFlow<Int> = preferencesRepository.dailyGoal
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2000)
    
    private val _selectedDate = MutableStateFlow(Calendar.getInstance())
    val selectedDate: StateFlow<Calendar> = _selectedDate.asStateFlow()
    
    val customCups: StateFlow<List<CustomCup>> = customCupRepository.getAllCustomCups()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val todayIntakes: StateFlow<List<WaterIntake>> = _selectedDate.flatMapLatest { date ->
        val bounds = getDayBoundsUseCase(date)
        waterRepository.getIntakesForDay(bounds.start, bounds.end)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val totalConsumed: StateFlow<Int> = _selectedDate.flatMapLatest { date ->
        val bounds = getDayBoundsUseCase(date)
        waterRepository.getTotalForDay(bounds.start, bounds.end)
    }.map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    fun addWater(volumeMl: Int, cupName: String) {
        viewModelScope.launch {
            waterRepository.addWaterIntake(
                volumeMl = volumeMl,
                timestamp = System.currentTimeMillis(),
                cupName = cupName
            )
        }
    }
    
    fun setWaterLevel(volumeMl: Int) {
        viewModelScope.launch {
            val current = totalConsumed.value
            val difference = volumeMl - current
            
            if (difference != 0) {
                waterRepository.addWaterIntake(
                    volumeMl = difference,
                    timestamp = System.currentTimeMillis(),
                    cupName = "Manual Adjustment"
                )
            }
        }
    }
    
    fun addCustomCup(name: String, volumeMl: Int) {
        viewModelScope.launch {
            customCupRepository.addCustomCup(name, volumeMl)
        }
    }
    
    fun deleteCustomCup(customCup: CustomCup) {
        viewModelScope.launch {
            customCupRepository.deleteCustomCup(customCup)
        }
    }
    
    fun setDailyGoal(goal: Int) {
        preferencesRepository.setDailyGoal(goal)
    }
    
    fun selectDate(date: Calendar) {
        _selectedDate.value = date
    }
}
