package com.compose.waterapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.compose.waterapp.data.repository.PreferencesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class WaterAppInstrumentedTest : KoinTest {
    
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    private val preferencesRepository: PreferencesRepository by inject()
    
    @Before
    fun setup() {
        runBlocking {
            preferencesRepository.setOnboardingCompleted(true)
            preferencesRepository.setDailyGoal(2000)
        }
        composeTestRule.waitForIdle()
        Thread.sleep(1500)
    }
    
    @Test
    fun testAddWaterWithSmallCup() {
        composeTestRule.waitForIdle()
        
        composeTestRule
            .onNodeWithText("Small Cup", useUnmergedTree = true)
            .assertExists()
            .performClick()
        
        composeTestRule.waitForIdle()
        Thread.sleep(1000)
        
        composeTestRule
            .onNodeWithText("200", useUnmergedTree = true)
            .assertExists()
    }
    
    @Test
    fun testNavigateToSettings() {
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("Settings")
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(500)

        composeTestRule
            .onAllNodesWithText("Settings", useUnmergedTree = true)
            .onFirst()
            .assertExists()
    }

    @Test
    fun testNavigateToReports() {
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("Reports")
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(500)

        composeTestRule
            .onAllNodesWithText("Reports", useUnmergedTree = true)
            .onFirst()
            .assertExists()
    }
    
    @Test
    fun testDefaultCupsAreVisible() {
        composeTestRule.waitForIdle()
        
        composeTestRule
            .onNodeWithText("Small Cup", useUnmergedTree = true)
            .assertExists()
        
        composeTestRule
            .onNodeWithText("Medium Cup", useUnmergedTree = true)
            .assertExists()
    }

}
