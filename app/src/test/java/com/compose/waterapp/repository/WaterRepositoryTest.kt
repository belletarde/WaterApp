package com.compose.waterapp.repository

import com.compose.waterapp.data.dao.WaterIntakeDao
import com.compose.waterapp.data.entity.WaterIntake
import com.compose.waterapp.data.repository.WaterRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class WaterRepositoryTest {

    private lateinit var waterIntakeDao: WaterIntakeDao
    private lateinit var repository: WaterRepository

    @Before
    fun setup() {
        waterIntakeDao = mockk(relaxed = true)
        repository = WaterRepository(waterIntakeDao)
    }

    @Test
    fun `addWaterIntake should insert into database`() = runTest {
        repository.addWaterIntake(200, 123456789L, "Small Cup")

        coVerify {
            waterIntakeDao.insert(match {
                it.volumeMl == 200 &&
                it.timestamp == 123456789L &&
                it.cupName == "Small Cup"
            })
        }
    }

    @Test
    fun `getIntakesForDay should return intakes from dao`() = runTest {
        val mockIntakes = listOf(
            WaterIntake(1, 200, 123456789L, "Cup 1"),
            WaterIntake(2, 300, 123456790L, "Cup 2")
        )
        
        every { waterIntakeDao.getIntakesForDay(any(), any()) } returns flowOf(mockIntakes)

        val result = repository.getIntakesForDay(0L, 1000L).first()

        assertEquals(2, result.size)
        assertEquals(200, result[0].volumeMl)
        assertEquals(300, result[1].volumeMl)
    }

    @Test
    fun `getTotalForDay should return sum from dao`() = runTest {
        every { waterIntakeDao.getTotalForDay(any(), any()) } returns flowOf(1500)

        val result = repository.getTotalForDay(0L, 1000L).first()

        assertEquals(1500, result)
    }

    @Test
    fun `getTotalForDay should return 0 when dao returns null`() = runTest {
        every { waterIntakeDao.getTotalForDay(any(), any()) } returns flowOf(null)

        val result = repository.getTotalForDay(0L, 1000L).first()

        assertEquals(0, result)
    }
}
