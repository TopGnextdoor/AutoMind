package com.automind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.automind.ui.components.GlassyButton
import com.automind.ui.theme.ElectricBlue
import com.automind.ui.theme.TextSecondary

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AutoMind",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            ),
            color = ElectricBlue
        )
        Text(
            text = "SYNC YOUR FOCUS",
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(32.dp))

        GlassyButton(
            text = "Login",
            onClick = { onLoginSuccess() }, // Simplified for demo
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { /* Handle Sign Up */ }) {
            Text("Don't have an account? Sign Up", color = TextSecondary)
        }

        Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.White.copy(alpha = 0.1f))

        GlassyButton(
            text = "Continue with Google",
            onClick = { onLoginSuccess() }, // Simplified for demo
            modifier = Modifier.fillMaxWidth(),
            isPrimary = false
        )
    }
}
