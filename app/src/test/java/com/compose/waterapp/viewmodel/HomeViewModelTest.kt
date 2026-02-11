package com.compose.waterapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.compose.waterapp.data.entity.CustomCup
import com.compose.waterapp.data.entity.WaterIntake
import com.compose.waterapp.data.repository.CustomCupRepository
import com.compose.waterapp.data.repository.PreferencesRepository
import com.compose.waterapp.data.repository.WaterRepository
import com.compose.waterapp.domain.usecase.GetDayBoundsUseCase
import com.compose.waterapp.ui.viewmodel.HomeViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var waterRepository: WaterRepository
    private lateinit var customCupRepository: CustomCupRepository
    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var getDayBoundsUseCase: GetDayBoundsUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        waterRepository = mockk(relaxed = true)
        customCupRepository = mockk(relaxed = true)
        preferencesRepository = mockk(relaxed = true)
        getDayBoundsUseCase = GetDayBoundsUseCase()

        every { preferencesRepository.dailyGoal } returns flowOf(2000)
        every { waterRepository.getTotalForDay(any(), any()) } returns flowOf(0)
        every { waterRepository.getIntakesForDay(any(), any()) } returns flowOf(emptyList())
        every { customCupRepository.getAllCustomCups() } returns flowOf(emptyList())

        viewModel = HomeViewModel(waterRepository, customCupRepository, preferencesRepository, getDayBoundsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addWater should call repository with correct parameters`() = runTest {
        viewModel.addWater(200, "Small Cup")
        
        advanceUntilIdle()

        coVerify {
            waterRepository.addWaterIntake(
                volumeMl = 200,
                timestamp = any(),
                cupName = "Small Cup"
            )
        }
    }

    @Test
    fun `setWaterLevel should add water when new volume is higher`() = runTest {
        every { waterRepository.getTotalForDay(any(), any()) } returns flowOf(500)
        
        viewModel = HomeViewModel(waterRepository, customCupRepository, preferencesRepository, getDayBoundsUseCase)
        advanceUntilIdle()

        viewModel.setWaterLevel(800)
        advanceUntilIdle()

        coVerify {
            waterRepository.addWaterIntake(
                volumeMl = 300,
                timestamp = any(),
                cupName = "Manual Adjustment"
            )
        }
    }

    @Test
    fun `setWaterLevel should remove water when new volume is lower`() = runTest {
        every { waterRepository.getTotalForDay(any(), any()) } returns flowOf(800)
        
        viewModel = HomeViewModel(waterRepository, customCupRepository, preferencesRepository, getDayBoundsUseCase)
        advanceUntilIdle()

        viewModel.setWaterLevel(500)
        advanceUntilIdle()

        coVerify {
            waterRepository.addWaterIntake(
                volumeMl = -300,
                timestamp = any(),
                cupName = "Manual Adjustment"
            )
        }
    }

    @Test
    fun `addCustomCup should call repository with correct parameters`() = runTest {
        viewModel.addCustomCup("My Cup", 350)
        
        advanceUntilIdle()

        coVerify {
            customCupRepository.addCustomCup("My Cup", 350)
        }
    }

    @Test
    fun `deleteCustomCup should call repository with correct cup`() = runTest {
        val cup = CustomCup(id = 1, name = "Test Cup", volumeMl = 250)
        
        viewModel.deleteCustomCup(cup)
        
        advanceUntilIdle()

        coVerify {
            customCupRepository.deleteCustomCup(cup)
        }
    }

    @Test
    fun `setDailyGoal should update preferences`() {
        viewModel.setDailyGoal(3000)

        verify {
            preferencesRepository.setDailyGoal(3000)
        }
    }

    @Test
    fun `dailyGoal flow should emit correct value`() = runTest {
        every { preferencesRepository.dailyGoal } returns flowOf(2500)
        
        viewModel = HomeViewModel(waterRepository, customCupRepository, preferencesRepository, getDayBoundsUseCase)
        advanceUntilIdle()

        assertEquals(2500, viewModel.dailyGoal.value)
    }

    @Test
    fun `totalConsumed flow should emit correct value`() = runTest {
        every { waterRepository.getTotalForDay(any(), any()) } returns flowOf(1500)
        
        viewModel = HomeViewModel(waterRepository, customCupRepository, preferencesRepository, getDayBoundsUseCase)
        advanceUntilIdle()

        assertEquals(1500, viewModel.totalConsumed.value)
    }

    @Test
    fun `customCups flow should emit correct list`() = runTest {
        val cups = listOf(
            CustomCup(1, "Cup 1", 300),
            CustomCup(2, "Cup 2", 400)
        )
        every { customCupRepository.getAllCustomCups() } returns flowOf(cups)
        
        viewModel = HomeViewModel(waterRepository, customCupRepository, preferencesRepository, getDayBoundsUseCase)
        advanceUntilIdle()

        assertEquals(2, viewModel.customCups.value.size)
        assertEquals("Cup 1", viewModel.customCups.value[0].name)
    }
}
