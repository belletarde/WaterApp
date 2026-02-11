package com.compose.waterapp.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.waterapp.R
import com.compose.waterapp.ui.components.WaterGlassView
import com.compose.waterapp.ui.theme.WaterAppTheme
import com.compose.waterapp.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToReports: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val dailyGoal by viewModel.dailyGoal.collectAsState()
    val totalConsumed by viewModel.totalConsumed.collectAsState()
    val customCups by viewModel.customCups.collectAsState()
    
    var showCustomCupDialog by remember { mutableStateOf(false) }
    
    val progress = (totalConsumed.toFloat() / dailyGoal).coerceIn(0f, 1f)
    val percentage = (progress * 100).toInt()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.app_name),
                        fontWeight = FontWeight.Medium
                    ) 
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onNavigateToReports) {
                        Icon(
                            Icons.Default.BarChart,
                            contentDescription = stringResource(R.string.reports),
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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WaterGlassView(
                    currentVolume = totalConsumed,
                    maxVolume = dailyGoal,
                    onVolumeChange = { newVolume ->
                        viewModel.setWaterLevel(newVolume)
                    },
                    modifier = Modifier.height(240.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$percentage%",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.daily_goal),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$totalConsumed ml / $dailyGoal ml",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_water),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    item {
                        ModernWaterCupCard(
                            icon = "💧",
                            label = stringResource(R.string.small_cup),
                            volume = 200,
                            onClick = { viewModel.addWater(200, "Small Cup") }
                        )
                    }
                    item {
                        ModernWaterCupCard(
                            icon = "🥤",
                            label = stringResource(R.string.medium_cup),
                            volume = 500,
                            onClick = { viewModel.addWater(500, "Medium Cup") }
                        )
                    }
                    
                    items(customCups) { cup ->
                        ModernWaterCupCard(
                            icon = "☕",
                            label = cup.name,
                            volume = cup.volumeMl,
                            isCustom = true,
                            onDelete = { viewModel.deleteCustomCup(cup) },
                            onClick = { viewModel.addWater(cup.volumeMl, cup.name) }
                        )
                    }
                    
                    item {
                        AddCupCard(
                            onClick = { showCustomCupDialog = true }
                        )
                    }
                }
            }
        }
    }
    
    if (showCustomCupDialog) {
        ModernCustomCupDialog(
            onDismiss = { showCustomCupDialog = false },
            onSave = { name, volume ->
                viewModel.addCustomCup(name, volume)
                showCustomCupDialog = false
            }
        )
    }
}

@Composable
fun ModernWaterCupCard(
    icon: String,
    label: String,
    volume: Int,
    isCustom: Boolean = false,
    onDelete: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Card(
        onClick = {
            isPressed = true
            onClick()
            isPressed = false
        },
        modifier = Modifier
            .width(110.dp)
            .shadow(
                elevation = if (isPressed) 2.dp else 6.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = icon,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$volume ${stringResource(R.string.ml_unit)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (isCustom && onDelete != null) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(28.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddCupCard(
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(110.dp)
            .semantics { contentDescription = "Add Cup" }
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Cup",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.add_cup),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = " ",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Transparent
            )
        }
    }
}

@Composable
fun ModernCustomCupDialog(
    onDismiss: () -> Unit,
    onSave: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var volume by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                stringResource(R.string.create_custom_cup),
                fontWeight = FontWeight.SemiBold
            ) 
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.cup_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = volume,
                    onValueChange = { volume = it.filter { char -> char.isDigit() } },
                    label = { Text(stringResource(R.string.cup_volume)) },
                    suffix = { Text("ml") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val vol = volume.toIntOrNull()
                    if (name.isNotBlank() && vol != null && vol > 0) {
                        onSave(name, vol)
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WaterAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WaterGlassView(
                        currentVolume = 800,
                        maxVolume = 2000,
                        onVolumeChange = {},
                        modifier = Modifier.height(240.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "40%",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Daily Goal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "800 ml / 2000 ml",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Add Water",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        item {
                            ModernWaterCupCard(
                                icon = "💧",
                                label = "Small Cup",
                                volume = 200,
                                onClick = {}
                            )
                        }
                        item {
                            ModernWaterCupCard(
                                icon = "🥤",
                                label = "Medium Cup",
                                volume = 500,
                                onClick = {}
                            )
                        }
                        item {
                            AddCupCard(onClick = {})
                        }
                    }
                }
            }
        }
    }
}
