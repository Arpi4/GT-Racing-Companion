package com.arpitoth.gtracingcompanion.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : NavItem("home", "Kezdőlap", Icons.Default.Home)
    object Drivers : NavItem("drivers", "Pilóták", Icons.Default.Person)
    object Teams : NavItem("teams", "Csapatok", Icons.Default.Group)
    object Races : NavItem("races", "Versenyek", Icons.Default.EmojiEvents)
    object Tracks : NavItem("tracks", "Pályák", Icons.Default.LocationOn)
    object Training : NavItem("training", "Edzés", Icons.Default.Timer)
}