package com.compose.waterapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreferencesRepository(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val _dailyGoal = MutableStateFlow(getDailyGoal())
    val dailyGoal: StateFlow<Int> = _dailyGoal
    
    private val _notificationsEnabled = MutableStateFlow(getNotificationsEnabled())
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled
    
    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }
    
    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }
    
    fun getWeight(): Float {
        return prefs.getFloat(KEY_WEIGHT, DEFAULT_WEIGHT)
    }
    
    fun setWeight(weight: Float) {
        prefs.edit().putFloat(KEY_WEIGHT, weight).apply()
    }
    
    fun getActivityLevel(): String {
        return prefs.getString(KEY_ACTIVITY_LEVEL, DEFAULT_ACTIVITY_LEVEL) ?: DEFAULT_ACTIVITY_LEVEL
    }
    
    fun setActivityLevel(level: String) {
        prefs.edit().putString(KEY_ACTIVITY_LEVEL, level).apply()
    }
    
    fun getDailyGoal(): Int {
        return prefs.getInt(KEY_DAILY_GOAL, DEFAULT_DAILY_GOAL)
    }
    
    fun setDailyGoal(goal: Int) {
        prefs.edit().putInt(KEY_DAILY_GOAL, goal).apply()
        _dailyGoal.value = goal
    }
    
    fun getNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply()
        _notificationsEnabled.value = enabled
    }
    
    fun getNotificationStartHour(): Int {
        return prefs.getInt(KEY_START_HOUR, DEFAULT_START_HOUR)
    }
    
    fun setNotificationStartHour(hour: Int) {
        prefs.edit().putInt(KEY_START_HOUR, hour).apply()
    }
    
    fun getNotificationEndHour(): Int {
        return prefs.getInt(KEY_END_HOUR, DEFAULT_END_HOUR)
    }
    
    fun setNotificationEndHour(hour: Int) {
        prefs.edit().putInt(KEY_END_HOUR, hour).apply()
    }
    
    fun getNotificationInterval(): Int {
        return prefs.getInt(KEY_INTERVAL, DEFAULT_INTERVAL)
    }
    
    fun setNotificationInterval(interval: Int) {
        prefs.edit().putInt(KEY_INTERVAL, interval).apply()
    }
    
    companion object {
        private const val PREFS_NAME = "water_app_prefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_WEIGHT = "weight"
        private const val KEY_ACTIVITY_LEVEL = "activity_level"
        private const val KEY_DAILY_GOAL = "daily_goal"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_START_HOUR = "start_hour"
        private const val KEY_END_HOUR = "end_hour"
        private const val KEY_INTERVAL = "interval"
        
        private const val DEFAULT_WEIGHT = 70f
        private const val DEFAULT_ACTIVITY_LEVEL = "moderate"
        private const val DEFAULT_DAILY_GOAL = 2000
        private const val DEFAULT_START_HOUR = 9
        private const val DEFAULT_END_HOUR = 21
        private const val DEFAULT_INTERVAL = 1
    }
}
