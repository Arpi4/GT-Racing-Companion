package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.arpitoth.gtracingcompanion.R
import com.arpitoth.gtracingcompanion.data.AuthRepository
import com.arpitoth.gtracingcompanion.data.GtRepository
import com.arpitoth.gtracingcompanion.data.UserRole
import com.arpitoth.gtracingcompanion.data.local.entity.RaceEntity
import com.arpitoth.gtracingcompanion.data.local.entity.TrainingEntity
import com.arpitoth.gtracingcompanion.ui.theme.Spacing
import com.arpitoth.gtracingcompanion.ui.theme.Shadows
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.contentDescription
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    repository: GtRepository,
    authRepository: AuthRepository
) {
    val driverCount by repository.driverCount.collectAsStateWithLifecycle(initialValue = 0)
    val teamCount by repository.teamCount.collectAsStateWithLifecycle(initialValue = 0)
    val raceCount by repository.raceCount.collectAsStateWithLifecycle(initialValue = 0)
    val trackCount by repository.trackCount.collectAsStateWithLifecycle(initialValue = 0)
    val upcoming by repository.dashboardUpcomingRaces.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val recentTrainings by repository.dashboardRecentTrainings.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val widthDp = LocalConfiguration.current.screenWidthDp
    val useWideLayout = widthDp >= 720
    val currentUser by authRepository.currentUser.collectAsStateWithLifecycle(initialValue = null)
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Spacing.medium, vertical = Spacing.medium)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = Spacing.medium)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "GT Racing Companion logo",
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(Spacing.small))
                Text(
                    text = "GT RACING COMPANION",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = 0.8.sp
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bejelentkezve: ${currentUser?.email ?: "ismeretlen"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedButton(onClick = {
                    scope.launch {
                        authRepository.logout()
                        navController.navigate("auth") {
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }) {
                    Text("Kijelentkezés")
                }
            }
            if (currentUser?.role == UserRole.ADMIN) {
                Text(
                    text = "Szerepkör: ADMIN (admin funkciók elérhetők)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = Spacing.small)
                )
            } else {
                Text(
                    text = "Szerepkör: MEMBER (korlátozott funkciók)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = Spacing.small)
                )
            }
            Spacer(Modifier.height(Spacing.medium))
        }

        item {
            WelcomeBanner(
                modifier = Modifier
                    .semantics { heading() }
                    .padding(bottom = Spacing.large)
            )
        }

        item {
            StatsGrid(
                pilots = driverCount,
                teams = teamCount,
                races = raceCount,
                tracks = trackCount,
                onPilots = { navController.navigate("drivers") },
                onTeams = { navController.navigate("teams") },
                onRaces = { navController.navigate("races") },
                onTracks = { navController.navigate("tracks") }
            )
        }

        if (!useWideLayout) {
            item {
                Spacer(Modifier.height(Spacing.large))
                Text(
                    "KÖZELGŐ VERSENYEK",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(bottom = Spacing.small)
                )
            }

            items(upcoming) { race ->
                UpcomingRaceCard(race)
            }

            item {
                Spacer(Modifier.height(Spacing.large))
                Text(
                    "LEGUTÓBBI EDZÉSEK",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(bottom = Spacing.small)
                )
            }

            items(recentTrainings) { training ->
                RecentTrainingCard(training)
            }
        } else {
            item {
                Spacer(Modifier.height(Spacing.large))
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.medium)
                ) {
                    val (upRef, recentRef) = createRefs()

                    Column(
                        modifier = Modifier.constrainAs(upRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(recentRef.start, margin = Spacing.medium)
                            width = Dimension.fillToConstraints
                        }
                    ) {
                        Text(
                            "KÖZELGŐ VERSENYEK",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.2.sp,
                            modifier = Modifier.padding(bottom = Spacing.small)
                        )
                        upcoming.forEach { race ->
                            UpcomingRaceCard(race)
                        }
                    }

                    Column(
                        modifier = Modifier.constrainAs(recentRef) {
                            start.linkTo(upRef.end)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                    ) {
                        Text(
                            "LEGUTÓBBI EDZÉSEK",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.2.sp,
                            modifier = Modifier.padding(bottom = Spacing.small)
                        )
                        recentTrainings.forEach { training ->
                            RecentTrainingCard(training)
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(Spacing.extraLarge)) }
    }
}

@Composable
private fun WelcomeBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF1A1A1A),
                            Color(0xFF0D2818),
                            Color(0xFF121212)
                        )
                    )
                )
                .padding(Spacing.large)
        ) {
            Column {
                Text(
                    "Üdvözöllek!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(Spacing.small))
                Text(
                    "Kövesd nyomon a pilótáidat, csapataidat és versenyeidet egyetlen helyen.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFB0B0B0)
                )
            }
        }
    }
}

@Composable
private fun StatsGrid(
    pilots: Int,
    teams: Int,
    races: Int,
    tracks: Int,
    onPilots: () -> Unit,
    onTeams: () -> Unit,
    onRaces: () -> Unit,
    onTracks: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            StatTile(
                icon = Icons.Default.Person,
                label = "Pilóták",
                value = pilots.toString(),
                onClick = onPilots,
                modifier = Modifier.weight(1f)
            )
            StatTile(
                icon = Icons.Default.Group,
                label = "Csapatok",
                value = teams.toString(),
                onClick = onTeams,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            StatTile(
                icon = Icons.Default.EmojiEvents,
                label = "Versenyek",
                value = races.toString(),
                onClick = onRaces,
                modifier = Modifier.weight(1f)
            )
            StatTile(
                icon = Icons.Default.LocationOn,
                label = "Pályák",
                value = tracks.toString(),
                onClick = onTracks,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatTile(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = "$label: $value"
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Shadows.small)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(Spacing.medium))
            Column(Modifier.weight(1f)) {
                Text(
                    label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun UpcomingRaceCard(race: RaceEntity) {
    val fontScale = LocalDensity.current.fontScale
    val maxLines = if (fontScale > 1.2f) 2 else 1
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(vertical = Spacing.extraSmall),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    race.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${race.location} · ${race.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ) {
                Text(
                    "Közelgő",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = Spacing.small, vertical = Spacing.extraSmall)
                )
            }
        }
    }
}

@Composable
private fun RecentTrainingCard(item: TrainingEntity) {
    val fontScale = LocalDensity.current.fontScale
    val maxLines = if (fontScale > 1.2f) 2 else 1
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(vertical = Spacing.extraSmall),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(Spacing.medium))
            Column(Modifier.weight(1f)) {
                Text(
                    item.driverName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${item.type} · ${item.durationMinutes} perc · ${item.rating}/10",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
