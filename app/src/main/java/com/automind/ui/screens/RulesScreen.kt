package com.automind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.automind.data.model.RuleEntity
import com.automind.ui.components.GradientCard
import com.automind.ui.theme.ElectricBlue
import com.automind.ui.theme.TextSecondary
import com.automind.ui.viewmodel.AutoMindViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(viewModel: AutoMindViewModel) {
    val rules by viewModel.allRules.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    viewModel.addRule(RuleEntity(
                        title = "New Rule",
                        description = "Block YouTube after 10 PM",
                        targetPackage = "com.google.android.youtube",
                        startTime = "22:00",
                        endTime = "06:00"
                    ))
                },
                containerColor = ElectricBlue,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Rule")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Automation Rules",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(rules) { rule ->
                RuleCard(rule, onUpdate = { viewModel.updateRule(it) })
            }
        }
    }
}

@Composable
fun RuleCard(rule: RuleEntity, onUpdate: (RuleEntity) -> Unit) {
    GradientCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = rule.title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "${rule.description} (${rule.startTime} - ${rule.endTime})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            Switch(
                checked = rule.isEnabled,
                onCheckedChange = { onUpdate(rule.copy(isEnabled = it)) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ElectricBlue,
                    checkedTrackColor = ElectricBlue.copy(alpha = 0.5f)
                )
            )
        }
    }
}
