package com.compose.waterapp.ui.navigation

import androidx.compose.runtime.*
import com.compose.waterapp.data.repository.PreferencesRepository
import com.compose.waterapp.ui.screen.HomeScreen
import com.compose.waterapp.ui.screen.OnboardingScreen
import com.compose.waterapp.ui.screen.ReportsScreen
import com.compose.waterapp.ui.screen.SettingsScreen
import com.compose.waterapp.ui.screen.SplashScreen
import org.koin.compose.koinInject

enum class Screen {
    Splash,
    Onboarding,
    Home,
    Reports,
    Settings
}

@Composable
fun Navigation(
    preferencesRepository: PreferencesRepository = koinInject()
) {
    val isOnboardingCompleted = remember { preferencesRepository.isOnboardingCompleted() }
    var currentScreen by remember { mutableStateOf(Screen.Splash) }
    
    when (currentScreen) {
        Screen.Splash -> {
            SplashScreen(
                onSplashFinished = {
                    currentScreen = if (isOnboardingCompleted) Screen.Home else Screen.Onboarding
                }
            )
        }
        Screen.Onboarding -> {
            OnboardingScreen(
                onComplete = { currentScreen = Screen.Home }
            )
        }
        Screen.Home -> {
            HomeScreen(
                onNavigateToReports = { currentScreen = Screen.Reports },
                onNavigateToSettings = { currentScreen = Screen.Settings }
            )
        }
        Screen.Reports -> {
            ReportsScreen(
                onNavigateBack = { currentScreen = Screen.Home }
            )
        }
        Screen.Settings -> {
            SettingsScreen(
                onNavigateBack = { currentScreen = Screen.Home }
            )
        }
    }
}
