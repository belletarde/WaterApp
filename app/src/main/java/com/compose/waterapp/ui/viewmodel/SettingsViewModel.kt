package com.compose.waterapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.waterapp.data.repository.PreferencesRepository
import com.compose.waterapp.domain.model.ActivityLevel
import com.compose.waterapp.domain.usecase.CalculateDailyGoalUseCase
import com.compose.waterapp.notification.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val notificationScheduler: NotificationScheduler,
    private val calculateDailyGoalUseCase: CalculateDailyGoalUseCase
) : ViewModel() {
    
    val notificationsEnabled: StateFlow<Boolean> = preferencesRepository.notificationsEnabled
    
    private val _startHour = MutableStateFlow(preferencesRepository.getNotificationStartHour())
    val startHour: StateFlow<Int> = _startHour.asStateFlow()
    
    private val _endHour = MutableStateFlow(preferencesRepository.getNotificationEndHour())
    val endHour: StateFlow<Int> = _endHour.asStateFlow()
    
    private val _interval = MutableStateFlow(preferencesRepository.getNotificationInterval())
    val interval: StateFlow<Int> = _interval.asStateFlow()
    
    private val _weight = MutableStateFlow(preferencesRepository.getWeight())
    val weight: StateFlow<Float> = _weight.asStateFlow()
    
    private val _activityLevel = MutableStateFlow(preferencesRepository.getActivityLevel())
    val activityLevel: StateFlow<String> = _activityLevel.asStateFlow()
    
    init {
        if (preferencesRepository.getNotificationsEnabled()) {
            scheduleNotifications()
        }
    }
    
    fun setWeight(weight: Float) {
        _weight.value = weight
        preferencesRepository.setWeight(weight)
        updateDailyGoal()
    }
    
    fun setActivityLevel(level: String) {
        _activityLevel.value = level
        preferencesRepository.setActivityLevel(level)
        updateDailyGoal()
    }
    
    private fun updateDailyGoal() {
        val activityLevel = ActivityLevel.fromString(_activityLevel.value)
        val newGoal = calculateDailyGoalUseCase(_weight.value, activityLevel)
        preferencesRepository.setDailyGoal(newGoal)
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        preferencesRepository.setNotificationsEnabled(enabled)
        if (enabled) {
            scheduleNotifications()
        } else {
            notificationScheduler.cancelAllNotifications()
        }
    }
    
    fun setStartHour(hour: Int) {
        _startHour.value = hour
        preferencesRepository.setNotificationStartHour(hour)
        if (preferencesRepository.getNotificationsEnabled()) {
            scheduleNotifications()
        }
    }
    
    fun setEndHour(hour: Int) {
        _endHour.value = hour
        preferencesRepository.setNotificationEndHour(hour)
        if (preferencesRepository.getNotificationsEnabled()) {
            scheduleNotifications()
        }
    }
    
    fun setInterval(interval: Int) {
        _interval.value = interval
        preferencesRepository.setNotificationInterval(interval)
        if (preferencesRepository.getNotificationsEnabled()) {
            scheduleNotifications()
        }
    }
    
    private fun scheduleNotifications() {
        viewModelScope.launch {
            notificationScheduler.scheduleNotifications(
                startHour = _startHour.value,
                endHour = _endHour.value,
                intervalHours = _interval.value
            )
        }
    }
}
