package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.arpitoth.gtracingcompanion.data.AuthRepository
import com.arpitoth.gtracingcompanion.ui.theme.Spacing
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    navController: NavController,
    authRepository: AuthRepository
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.medium),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bejelentkezés", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(Spacing.medium))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_email")
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Jelszó") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_password")
        )
        Spacer(modifier = Modifier.height(Spacing.medium))

        Button(
            onClick = {
                scope.launch {
                    val ok = authRepository.login(email, password)
                    if (ok) {
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        message = "Sikertelen bejelentkezés."
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_login_button")
        ) {
            Text("Bejelentkezés")
        }
        Spacer(modifier = Modifier.height(Spacing.small))
        Button(
            onClick = {
                scope.launch {
                    val ok = authRepository.register(email, password)
                    if (ok) {
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        message = "Sikertelen regisztráció. Ellenőrizd az emailt és legalább 6 karakteres jelszót adj meg."
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_register_button")
        ) {
            Text("Regisztráció")
        }
        if (message.isNotBlank()) {
            Spacer(modifier = Modifier.height(Spacing.small))
            Text(message, color = MaterialTheme.colorScheme.error)
        }
    }
}
