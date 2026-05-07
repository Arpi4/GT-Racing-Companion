package com.arpitoth.gtracingcompanion.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arpitoth.gtracingcompanion.data.AuthRepository
import com.arpitoth.gtracingcompanion.data.GtRepository
import com.arpitoth.gtracingcompanion.ui.screens.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    repository: GtRepository,
    authRepository: AuthRepository
) {
    val currentUser by authRepository.currentUser.collectAsStateWithLifecycle(initialValue = null)
    val isLoggedIn = currentUser != null

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "auth",
        modifier = modifier
    ) {
        composable("auth") {
            if (isLoggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            } else {
                AuthScreen(navController = navController, authRepository = authRepository)
            }
        }

        composable("home") {
            if (!isLoggedIn) {
                navController.navigate("auth") {
                    popUpTo("home") { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                HomeScreen(
                    navController = navController,
                    repository = repository,
                    authRepository = authRepository
                )
            }
        }

        composable("drivers") {
            if (!isLoggedIn) {
                navController.navigate("auth") { launchSingleTop = true }
            } else {
                DriversScreen(navController = navController, repository = repository)
            }
        }

        composable("teams") {
            if (!isLoggedIn) {
                navController.navigate("auth") { launchSingleTop = true }
            } else {
                TeamsScreen(navController = navController, repository = repository)
            }
        }

        composable("races") {
            if (!isLoggedIn) {
                navController.navigate("auth") { launchSingleTop = true }
            } else {
                RacesScreen(navController = navController, repository = repository)
            }
        }

        composable("training") {
            if (!isLoggedIn) {
                navController.navigate("auth") { launchSingleTop = true }
            } else {
                TrainingScreen(navController = navController, repository = repository)
            }
        }

        composable("tracks") {
            if (!isLoggedIn) {
                navController.navigate("auth") { launchSingleTop = true }
            } else {
                TracksScreen(navController = navController, repository = repository)
            }
        }

        composable("add_driver") {
            if (!isLoggedIn) navController.navigate("auth") { launchSingleTop = true }
            else AddDriverScreen(navController = navController, repository = repository)
        }
        composable("add_team") {
            if (!isLoggedIn) navController.navigate("auth") { launchSingleTop = true }
            else AddTeamScreen(navController = navController, repository = repository)
        }
        composable("add_race") {
            if (!isLoggedIn) navController.navigate("auth") { launchSingleTop = true }
            else AddRaceScreen(navController = navController, repository = repository)
        }
        composable("add_training") {
            if (!isLoggedIn) navController.navigate("auth") { launchSingleTop = true }
            else AddTrainingScreen(navController = navController, repository = repository)
        }
        composable("add_track") {
            if (!isLoggedIn) navController.navigate("auth") { launchSingleTop = true }
            else AddTrackScreen(navController = navController, repository = repository)
        }
    }
}
