package com.compose.waterapp.di

import androidx.room.Room
import com.compose.waterapp.data.database.WaterDatabase
import com.compose.waterapp.data.repository.CustomCupRepository
import com.compose.waterapp.data.repository.PreferencesRepository
import com.compose.waterapp.data.repository.WaterRepository
import com.compose.waterapp.domain.usecase.*
import com.compose.waterapp.notification.NotificationScheduler
import com.compose.waterapp.ui.viewmodel.HomeViewModel
import com.compose.waterapp.ui.viewmodel.ReportsViewModel
import com.compose.waterapp.ui.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    
    single {
        Room.databaseBuilder(
            androidContext(),
            WaterDatabase::class.java,
            "water_database"
        ).build()
    }
    
    single { get<WaterDatabase>().waterIntakeDao() }
    single { get<WaterDatabase>().customCupDao() }
    
    single { WaterRepository(get()) }
    single { CustomCupRepository(get()) }
    single { PreferencesRepository(androidContext()) }
    
    single { NotificationScheduler(androidContext()) }
    
    factory { CalculateDailyGoalUseCase() }
    factory { GetDayBoundsUseCase() }
    factory { GetWeekBoundsUseCase() }
    factory { AggregateWeeklyDataUseCase() }
    factory { AggregateHourlyDataUseCase() }
    
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { ReportsViewModel(get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
}
