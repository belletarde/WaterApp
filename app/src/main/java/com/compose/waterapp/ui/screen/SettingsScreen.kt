package com.compose.waterapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.compose.waterapp.R
import com.compose.waterapp.ui.viewmodel.HomeViewModel
import com.compose.waterapp.ui.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    homeViewModel: HomeViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val dailyGoal by homeViewModel.dailyGoal.collectAsState()
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()
    val startHour by settingsViewModel.startHour.collectAsState()
    val endHour by settingsViewModel.endHour.collectAsState()
    val interval by settingsViewModel.interval.collectAsState()
    val weight by settingsViewModel.weight.collectAsState()
    val activityLevel by settingsViewModel.activityLevel.collectAsState()
    
    var showGoalDialog by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.settings),
                        fontWeight = FontWeight.Medium
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black.copy(alpha = 0.05f),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(R.string.daily_goal_setting),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "$dailyGoal ${stringResource(R.string.ml_unit)}",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.daily_goal),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Button(
                            onClick = { showGoalDialog = true },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(R.string.edit_goal))
                        }
                    }
                }
            }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black.copy(alpha = 0.05f),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(R.string.personal_info),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.weight),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${weight.toInt()} ${stringResource(R.string.kg_unit)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Button(
                            onClick = { showWeightDialog = true },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(R.string.edit))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Column {
                        Text(
                            text = stringResource(R.string.activity_level),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ActivityLevelChip(
                                label = stringResource(R.string.low),
                                isSelected = activityLevel == "low",
                                onClick = { settingsViewModel.setActivityLevel("low") },
                                modifier = Modifier.weight(1f)
                            )
                            ActivityLevelChip(
                                label = stringResource(R.string.moderate),
                                isSelected = activityLevel == "moderate",
                                onClick = { settingsViewModel.setActivityLevel("moderate") },
                                modifier = Modifier.weight(1f)
                            )
                            ActivityLevelChip(
                                label = stringResource(R.string.high),
                                isSelected = activityLevel == "high",
                                onClick = { settingsViewModel.setActivityLevel("high") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black.copy(alpha = 0.05f),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(R.string.notifications),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.enable_notifications),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { settingsViewModel.setNotificationsEnabled(it) }
                        )
                    }
                    
                    if (notificationsEnabled) {
                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Column {
                            Text(
                                text = stringResource(R.string.start_time),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Slider(
                                value = startHour.toFloat(),
                                onValueChange = { settingsViewModel.setStartHour(it.toInt()) },
                                valueRange = 0f..23f,
                                steps = 22,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = stringResource(R.string.time_format, startHour),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Column {
                            Text(
                                text = stringResource(R.string.end_time),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Slider(
                                value = endHour.toFloat(),
                                onValueChange = { settingsViewModel.setEndHour(it.toInt()) },
                                valueRange = 0f..23f,
                                steps = 22,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = stringResource(R.string.time_format, endHour),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Column {
                            Text(
                                text = stringResource(R.string.notification_interval),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Slider(
                                value = interval.toFloat(),
                                onValueChange = { settingsViewModel.setInterval(it.toInt()) },
                                valueRange = 1f..4f,
                                steps = 2,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "$interval ${if (interval == 1) stringResource(R.string.hour_label) else stringResource(R.string.hours_label)}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
    
    if (showGoalDialog) {
        ModernEditGoalDialog(
            currentGoal = dailyGoal,
            onDismiss = { showGoalDialog = false },
            onSave = { newGoal ->
                homeViewModel.setDailyGoal(newGoal)
                showGoalDialog = false
            }
        )
    }
    
    if (showWeightDialog) {
        ModernEditWeightDialog(
            currentWeight = weight,
            onDismiss = { showWeightDialog = false },
            onSave = { newWeight ->
                settingsViewModel.setWeight(newWeight)
                showWeightDialog = false
            }
        )
    }
}

@Composable
fun ModernEditGoalDialog(
    currentGoal: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var goalText by remember { mutableStateOf(currentGoal.toString()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                stringResource(R.string.edit_goal),
                fontWeight = FontWeight.SemiBold
            ) 
        },
        text = {
            OutlinedTextField(
                value = goalText,
                onValueChange = { goalText = it.filter { char -> char.isDigit() } },
                label = { Text(stringResource(R.string.daily_goal_setting)) },
                suffix = { Text("ml") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val goal = goalText.toIntOrNull()
                    if (goal != null && goal > 0) {
                        onSave(goal)
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun ModernEditWeightDialog(
    currentWeight: Float,
    onDismiss: () -> Unit,
    onSave: (Float) -> Unit
) {
    var weightText by remember { mutableStateOf(currentWeight.toInt().toString()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                stringResource(R.string.edit_weight),
                fontWeight = FontWeight.SemiBold
            ) 
        },
        text = {
            OutlinedTextField(
                value = weightText,
                onValueChange = { weightText = it.filter { char -> char.isDigit() } },
                label = { Text(stringResource(R.string.weight)) },
                suffix = { Text("kg") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val weight = weightText.toFloatOrNull()
                    if (weight != null && weight > 0) {
                        onSave(weight)
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun ActivityLevelChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
