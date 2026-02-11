package com.compose.waterapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.waterapp.R
import com.compose.waterapp.data.repository.PreferencesRepository
import com.compose.waterapp.domain.model.ActivityLevel
import com.compose.waterapp.domain.usecase.CalculateDailyGoalUseCase
import org.koin.compose.koinInject

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    preferencesRepository: PreferencesRepository = koinInject(),
    calculateDailyGoalUseCase: CalculateDailyGoalUseCase = koinInject()
) {
    var currentPage by remember { mutableIntStateOf(0) }
    var weight by remember { mutableStateOf("70") }
    var activityLevel by remember { mutableStateOf("moderate") }
    
    val calculatedGoal = remember(weight, activityLevel) {
        val w = weight.toFloatOrNull() ?: 70f
        val level = ActivityLevel.fromString(activityLevel)
        calculateDailyGoalUseCase(w, level)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentPage) 32.dp else 8.dp)
                            .background(
                                color = if (index == currentPage)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(50)
                            )
                    )
                    if (index < 2) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (currentPage) {
                    0 -> OnboardingPage1()
                    1 -> OnboardingPage2()
                    2 -> OnboardingPage3(
                        weight = weight,
                        onWeightChange = { weight = it },
                        activityLevel = activityLevel,
                        onActivityLevelChange = { activityLevel = it },
                        calculatedGoal = calculatedGoal
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentPage > 0) {
                    TextButton(
                        onClick = { currentPage-- },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                } else {
                    TextButton(
                        onClick = {
                            preferencesRepository.setDailyGoal(2000)
                            preferencesRepository.setOnboardingCompleted(true)
                            onComplete()
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.skip))
                    }
                }
                
                Button(
                    onClick = {
                        if (currentPage < 2) {
                            currentPage++
                        } else {
                            val w = weight.toFloatOrNull() ?: 70f
                            preferencesRepository.setWeight(w)
                            preferencesRepository.setActivityLevel(activityLevel)
                            preferencesRepository.setDailyGoal(calculatedGoal)
                            preferencesRepository.setOnboardingCompleted(true)
                            onComplete()
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        if (currentPage < 2) stringResource(R.string.next)
                        else stringResource(R.string.finish)
                    )
                    if (currentPage == 2) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingPage1() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "💧",
            fontSize = 80.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.onboarding_title_1),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.onboarding_desc_1),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        BenefitCard("🌡️", stringResource(R.string.benefit_temperature))
        Spacer(modifier = Modifier.height(12.dp))
        BenefitCard("🚀", stringResource(R.string.benefit_nutrients))
        Spacer(modifier = Modifier.height(12.dp))
        BenefitCard("❤️", stringResource(R.string.benefit_organs))
    }
}

@Composable
fun OnboardingPage2() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "📊",
            fontSize = 80.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.onboarding_title_2),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.onboarding_desc_2),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun OnboardingPage3(
    weight: String,
    onWeightChange: (String) -> Unit,
    activityLevel: String,
    onActivityLevelChange: (String) -> Unit,
    calculatedGoal: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.onboarding_title_3),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = weight,
            onValueChange = { onWeightChange(it.filter { char -> char.isDigit() || char == '.' }) },
            label = { Text(stringResource(R.string.weight_kg)) },
            suffix = { Text("kg") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.activity_level),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        ActivityLevelCard(
            title = stringResource(R.string.activity_low),
            description = stringResource(R.string.activity_low_desc),
            isSelected = activityLevel == "low",
            onClick = { onActivityLevelChange("low") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        ActivityLevelCard(
            title = stringResource(R.string.activity_moderate),
            description = stringResource(R.string.activity_moderate_desc),
            isSelected = activityLevel == "moderate",
            onClick = { onActivityLevelChange("moderate") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        ActivityLevelCard(
            title = stringResource(R.string.activity_high),
            description = stringResource(R.string.activity_high_desc),
            isSelected = activityLevel == "high",
            onClick = { onActivityLevelChange("high") }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.your_daily_goal),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$calculatedGoal ${stringResource(R.string.ml_unit)}",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.based_on_calculation),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun BenefitCard(icon: String, text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ActivityLevelCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isSelected) 4.dp else 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = if (isSelected)
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
