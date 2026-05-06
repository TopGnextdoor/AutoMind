package com.automind

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.automind.ui.navigation.AppNavigation
import com.automind.ui.navigation.BottomNavigationBar
import com.automind.ui.theme.AutoMindTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import com.automind.service.FocusService
import com.automind.ui.viewmodel.AutoMindViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Start the focus monitoring service
        val serviceIntent = Intent(this, FocusService::class.java)
        startForegroundService(serviceIntent)

        setContent {
            AutoMindTheme {
                val viewModel: AutoMindViewModel = viewModel()
                AutoMindApp(viewModel)
            }
        }
    }
}

@Composable
fun AutoMindApp(viewModel: AutoMindViewModel) {
    val navController = rememberNavController()
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(navController = navController, viewModel = viewModel)
        }
    }
}
