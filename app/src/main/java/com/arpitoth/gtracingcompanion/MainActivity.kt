package com.arpitoth.gtracingcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.arpitoth.gtracingcompanion.data.AuthRepository
import com.arpitoth.gtracingcompanion.data.GtRepository
import com.arpitoth.gtracingcompanion.ui.components.BottomNavBar
import com.arpitoth.gtracingcompanion.ui.navigation.AppNavigation
import com.arpitoth.gtracingcompanion.ui.navigation.NavItem
import com.arpitoth.gtracingcompanion.ui.theme.GTRacingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = remember { GtRepository(applicationContext) }
            val authRepository = remember { AuthRepository(applicationContext) }
            LaunchedEffect(Unit) {
                repository.migrateIdsIfNeeded()
                repository.seedIfEmpty()
            }
            GTRacingTheme {
                MainScreen(repository = repository, authRepository = authRepository)
            }
        }
    }
}

@Composable
fun MainScreen(repository: GtRepository, authRepository: AuthRepository) {
    val navController = rememberNavController()
    val currentUser by authRepository.currentUser.collectAsStateWithLifecycle(initialValue = null)
    val isLoggedIn = currentUser != null

    val navItems = listOf(
        NavItem.Home,
        NavItem.Drivers,
        NavItem.Teams,
        NavItem.Races,
        NavItem.Tracks,
        NavItem.Training
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (isLoggedIn) {
                BottomNavBar(navController, navItems)
            }
        }
    ) { paddingValues ->

        AppNavigation(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            repository = repository,
            authRepository = authRepository
        )
    }
}
