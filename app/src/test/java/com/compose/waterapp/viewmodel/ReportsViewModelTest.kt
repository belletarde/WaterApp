package com.compose.waterapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.compose.waterapp.data.entity.WaterIntake
import com.compose.waterapp.data.repository.WaterRepository
import com.compose.waterapp.domain.usecase.AggregateHourlyDataUseCase
import com.compose.waterapp.domain.usecase.AggregateWeeklyDataUseCase
import com.compose.waterapp.domain.usecase.GetWeekBoundsUseCase
import com.compose.waterapp.ui.viewmodel.ReportsViewModel
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ReportsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var waterRepository: WaterRepository
    private lateinit var getWeekBoundsUseCase: GetWeekBoundsUseCase
    private lateinit var aggregateWeeklyDataUseCase: AggregateWeeklyDataUseCase
    private lateinit var aggregateHourlyDataUseCase: AggregateHourlyDataUseCase
    private lateinit var viewModel: ReportsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        waterRepository = mockk(relaxed = true)
        getWeekBoundsUseCase = GetWeekBoundsUseCase()
        aggregateWeeklyDataUseCase = AggregateWeeklyDataUseCase()
        aggregateHourlyDataUseCase = AggregateHourlyDataUseCase()

        every { waterRepository.getIntakesForPeriod(any(), any()) } returns flowOf(emptyList())

        viewModel = ReportsViewModel(
            waterRepository,
            getWeekBoundsUseCase,
            aggregateWeeklyDataUseCase,
            aggregateHourlyDataUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `weeklyData should contain 7 days`() = runTest {
        advanceUntilIdle()

        assertEquals(7, viewModel.weeklyData.value.size)
    }

    @Test
    fun `hourlyData should contain 24 hours`() = runTest {
        advanceUntilIdle()

        assertEquals(24, viewModel.hourlyData.value.size)
    }

    @Test
    fun `weeklyData should calculate totals correctly`() = runTest {
        val mockIntakes = listOf(
            WaterIntake(1, 200, System.currentTimeMillis(), "Cup 1"),
            WaterIntake(2, 300, System.currentTimeMillis(), "Cup 2")
        )
        
        every { waterRepository.getIntakesForPeriod(any(), any()) } returns flowOf(mockIntakes)
        
        viewModel = ReportsViewModel(
            waterRepository,
            getWeekBoundsUseCase,
            aggregateWeeklyDataUseCase,
            aggregateHourlyDataUseCase
        )
        advanceUntilIdle()

        val totalInWeek = viewModel.weeklyData.value.sumOf { it.totalMl }
        assertEquals(500, totalInWeek)
    }

    @Test
    fun `hourlyData should group intakes by hour correctly`() = runTest {
        val currentTime = System.currentTimeMillis()
        val mockIntakes = listOf(
            WaterIntake(1, 200, currentTime, "Cup 1"),
            WaterIntake(2, 300, currentTime, "Cup 2")
        )
        
        every { waterRepository.getIntakesForPeriod(any(), any()) } returns flowOf(mockIntakes)
        
        viewModel = ReportsViewModel(
            waterRepository,
            getWeekBoundsUseCase,
            aggregateWeeklyDataUseCase,
            aggregateHourlyDataUseCase
        )
        advanceUntilIdle()

        val totalInHours = viewModel.hourlyData.value.sumOf { it.totalMl }
        assertEquals(500, totalInHours)
    }

    @Test
    fun `previousWeek should update selectedWeekStart`() = runTest {
        val initialWeek = viewModel.selectedWeekStart.value.clone()
        
        viewModel.previousWeek()
        advanceUntilIdle()

        assertTrue(viewModel.selectedWeekStart.value.timeInMillis < (initialWeek as java.util.Calendar).timeInMillis)
    }

    @Test
    fun `nextWeek should update selectedWeekStart`() = runTest {
        val initialWeek = viewModel.selectedWeekStart.value.clone()
        
        viewModel.nextWeek()
        advanceUntilIdle()

        assertTrue(viewModel.selectedWeekStart.value.timeInMillis > (initialWeek as java.util.Calendar).timeInMillis)
    }

    @Test
    fun `weeklyData should be ordered chronologically`() = runTest {
        advanceUntilIdle()

        val dates = viewModel.weeklyData.value.map { it.date }
        
        assertEquals(7, dates.size)
    }
}
