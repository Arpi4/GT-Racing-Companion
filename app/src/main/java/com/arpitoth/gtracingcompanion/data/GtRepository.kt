package com.arpitoth.gtracingcompanion.data

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.arpitoth.gtracingcompanion.data.local.entity.DriverEntity
import com.arpitoth.gtracingcompanion.data.local.entity.RaceEntity
import com.arpitoth.gtracingcompanion.data.local.entity.TeamEntity
import com.arpitoth.gtracingcompanion.data.local.entity.TrackEntity
import com.arpitoth.gtracingcompanion.data.local.entity.TrainingEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Perzisztencia DataStore Preferences + Gson (nincs Room / KSP).
 */
class GtRepository(private val context: Context) {

    private val dataStore = context.gtDataStore
    private val gson = Gson()

    private val driverListType = object : TypeToken<List<DriverEntity>>() {}.type
    private val teamListType = object : TypeToken<List<TeamEntity>>() {}.type
    private val raceListType = object : TypeToken<List<RaceEntity>>() {}.type
    private val trackListType = object : TypeToken<List<TrackEntity>>() {}.type
    private val trainingListType = object : TypeToken<List<TrainingEntity>>() {}.type

    val drivers: Flow<List<DriverEntity>> = jsonFlow(GtPreferenceKeys.drivers, driverListType)
    val teams: Flow<List<TeamEntity>> = jsonFlow(GtPreferenceKeys.teams, teamListType)
    val races: Flow<List<RaceEntity>> = jsonFlow(GtPreferenceKeys.races, raceListType)
    val tracks: Flow<List<TrackEntity>> = jsonFlow(GtPreferenceKeys.tracks, trackListType)
    val trainings: Flow<List<TrainingEntity>> = jsonFlow(GtPreferenceKeys.trainings, trainingListType)

    val driverCount: Flow<Int> = drivers.map { it.size }
    val teamCount: Flow<Int> = teams.map { it.size }
    val raceCount: Flow<Int> = races.map { it.size }
    val trackCount: Flow<Int> = tracks.map { it.size }

    val dashboardUpcomingRaces: Flow<List<RaceEntity>> = races.map { list ->
        list.filter { it.isUpcoming }.sortedBy { it.date }.take(6)
    }

    val dashboardRecentTrainings: Flow<List<TrainingEntity>> = trainings.map { list ->
        list.sortedByDescending { it.createdAt }.take(5)
    }

    private fun <T> jsonFlow(key: Preferences.Key<String>, type: java.lang.reflect.Type): Flow<List<T>> =
        dataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs ->
                val json = prefs[key] ?: "[]"
                @Suppress("UNCHECKED_CAST")
                (gson.fromJson(json, type) as? List<T>) ?: emptyList()
            }

    /**
     * Backward-compatibility / safety: if older stored JSON contains duplicate or missing ids,
     * normalize them so the UI can rely on stable identity.
     *
     * This does not merge items; it only reassigns duplicate/invalid ids to new unique values.
     */
    suspend fun migrateIdsIfNeeded() = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val drivers = readDrivers(prefs)
            val normalizedDrivers = normalizeDriverIds(drivers)
            if (normalizedDrivers != drivers) writeDrivers(prefs, normalizedDrivers)

            val teams = readTeams(prefs)
            val normalizedTeams = normalizeTeamIds(teams)
            if (normalizedTeams != teams) writeTeams(prefs, normalizedTeams)

            val races = readRaces(prefs)
            val normalizedRaces = normalizeRaceIds(races)
            if (normalizedRaces != races) writeRaces(prefs, normalizedRaces)

            val tracks = readTracks(prefs)
            val normalizedTracks = normalizeTrackIds(tracks)
            if (normalizedTracks != tracks) writeTracks(prefs, normalizedTracks)

            val trainings = readTrainings(prefs)
            val normalizedTrainings = normalizeTrainingIds(trainings)
            if (normalizedTrainings != trainings) writeTrainings(prefs, normalizedTrainings)
        }
    }

    private fun normalizeDriverIds(list: List<DriverEntity>): List<DriverEntity> {
        val used = HashSet<Long>()
        var nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1L
        return list.map { entity ->
            val id = entity.id
            val valid = id > 0L && used.add(id)
            if (valid) entity else entity.copy(id = nextId++).also { used.add(it.id) }
        }
    }

    private fun normalizeTeamIds(list: List<TeamEntity>): List<TeamEntity> {
        val used = HashSet<Long>()
        var nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1L
        return list.map { entity ->
            val id = entity.id
            val valid = id > 0L && used.add(id)
            if (valid) entity else entity.copy(id = nextId++).also { used.add(it.id) }
        }
    }

    private fun normalizeRaceIds(list: List<RaceEntity>): List<RaceEntity> {
        val used = HashSet<Long>()
        var nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1L
        return list.map { entity ->
            val id = entity.id
            val valid = id > 0L && used.add(id)
            if (valid) entity else entity.copy(id = nextId++).also { used.add(it.id) }
        }
    }

    private fun normalizeTrackIds(list: List<TrackEntity>): List<TrackEntity> {
        val used = HashSet<Long>()
        var nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1L
        return list.map { entity ->
            val id = entity.id
            val valid = id > 0L && used.add(id)
            if (valid) entity else entity.copy(id = nextId++).also { used.add(it.id) }
        }
    }

    private fun normalizeTrainingIds(list: List<TrainingEntity>): List<TrainingEntity> {
        val used = HashSet<Long>()
        var nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1L
        return list.map { entity ->
            val id = entity.id
            val valid = id > 0L && used.add(id)
            if (valid) entity else entity.copy(id = nextId++).also { used.add(it.id) }
        }
    }

    suspend fun addDriver(name: String, age: Int, team: String = "") = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val list = readDrivers(prefs)
            val nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1L
            val cleanName = InputSecurity.cleanName(name)
            if (cleanName.isBlank()) return@edit
            writeDrivers(
                prefs,
                list + DriverEntity(
                    id = nextId,
                    name = cleanName,
                    team = InputSecurity.cleanName(team),
                    age = InputSecurity.clampAge(age)
                )
            )
        }
    }

    suspend fun updateDriver(entity: DriverEntity) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val list = readDrivers(prefs)
            val cleanName = InputSecurity.cleanName(entity.name)
            if (cleanName.isBlank()) return@edit
            val normalized = entity.copy(
                name = cleanName,
                team = InputSecurity.cleanName(entity.team),
                age = InputSecurity.clampAge(entity.age)
            )
            writeDrivers(prefs, list.map { if (it.id == entity.id) normalized else it })
        }
    }

    suspend fun deleteDriver(id: Long) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val drivers = readDrivers(prefs)
            val removed = drivers.firstOrNull { it.id == id } ?: return@edit
            writeDrivers(prefs, drivers.filterNot { it.id == id })
            val trainings = readTrainings(prefs)
            writeTrainings(prefs, trainings.filterNot { it.driverName == removed.name })
        }
    }

    suspend fun addTeam(name: String, country: String) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val list = readTeams(prefs)
            val nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1L
            val cleanName = InputSecurity.cleanName(name)
            if (cleanName.isBlank()) return@edit
            writeTeams(
                prefs,
                list + TeamEntity(
                    id = nextId,
                    name = cleanName,
                    country = InputSecurity.cleanName(country)
                )
            )
        }
    }

    suspend fun updateTeam(entity: TeamEntity) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val list = readTeams(prefs)
            val cleanName = InputSecurity.cleanName(entity.name)
            if (cleanName.isBlank()) return@edit
            val normalized = entity.copy(
                name = cleanName,
                country = InputSecurity.cleanName(entity.country)
            )
            writeTeams(prefs, list.map { if (it.id == entity.id) normalized else it })
        }
    }

    suspend fun deleteTeam(id: Long) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val teams = readTeams(prefs)
            val removed = teams.firstOrNull { it.id == id } ?: return@edit
            writeTeams(prefs, teams.filterNot { it.id == id })

            val drivers = readDrivers(prefs)
            writeDrivers(
                prefs,
                drivers.map { driver ->
                    if (driver.team == removed.name) driver.copy(team = "") else driver
                }
            )
        }
    }

    suspend fun addRace(title: String, location: String, date: String) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val list = readRaces(prefs)
            val nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1L
            val cleanTitle = InputSecurity.cleanTitle(title)
            if (cleanTitle.isBlank()) return@edit
            writeRaces(
                prefs,
                list + RaceEntity(
                    id = nextId,
                    date = InputSecurity.cleanText(date, maxLength = 16),
                    title = cleanTitle,
                    location = InputSecurity.cleanLocation(location),
                    isUpcoming = true
                )
            )
        }
    }

    suspend fun updateRace(entity: RaceEntity) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val list = readRaces(prefs)
            val cleanTitle = InputSecurity.cleanTitle(entity.title)
            if (cleanTitle.isBlank()) return@edit
            val normalized = entity.copy(
                title = cleanTitle,
                location = InputSecurity.cleanLocation(entity.location),
                date = InputSecurity.cleanText(entity.date, maxLength = 16)
            )
            writeRaces(prefs, list.map { if (it.id == entity.id) normalized else it })
        }
    }

    suspend fun deleteRace(id: Long) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val races = readRaces(prefs)
            writeRaces(prefs, races.filterNot { it.id == id })
        }
    }

    suspend fun addTrack(name: String, lengthKm: String, location: String = "") =
        withContext(Dispatchers.IO) {
            dataStore.edit { prefs ->
                val list = readTracks(prefs)
                val nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1L
                val cleanName = InputSecurity.cleanName(name)
                if (cleanName.isBlank()) return@edit
                writeTracks(
                    prefs,
                    list + TrackEntity(
                        id = nextId,
                        name = cleanName,
                        lengthKm = InputSecurity.cleanText(lengthKm, maxLength = 12),
                        location = InputSecurity.cleanLocation(location)
                    )
                )
            }
        }

    suspend fun updateTrack(entity: TrackEntity) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val list = readTracks(prefs)
            val cleanName = InputSecurity.cleanName(entity.name)
            if (cleanName.isBlank()) return@edit
            val normalized = entity.copy(
                name = cleanName,
                lengthKm = InputSecurity.cleanText(entity.lengthKm, maxLength = 12),
                location = InputSecurity.cleanLocation(entity.location)
            )
            writeTracks(prefs, list.map { if (it.id == entity.id) normalized else it })
        }
    }

    suspend fun deleteTrack(id: Long) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val tracks = readTracks(prefs)
            writeTracks(prefs, tracks.filterNot { it.id == id })
        }
    }

    suspend fun addTraining(driverName: String, type: String, durationMinutes: Int, rating: Int = 0) =
        withContext(Dispatchers.IO) {
            dataStore.edit { prefs ->
                val list = readTrainings(prefs)
                val nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1L
                val cleanType = InputSecurity.cleanTrainingType(type)
                if (cleanType.isBlank()) return@edit
                writeTrainings(
                    prefs,
                    list + TrainingEntity(
                        id = nextId,
                        driverName = InputSecurity.cleanName(driverName).ifBlank { "—" },
                        type = cleanType,
                        durationMinutes = InputSecurity.clampDurationMinutes(durationMinutes),
                        rating = InputSecurity.clampRating(rating),
                        createdAt = System.currentTimeMillis()
                    )
                )
            }
        }

    suspend fun updateTraining(entity: TrainingEntity) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val list = readTrainings(prefs)
            val cleanType = InputSecurity.cleanTrainingType(entity.type)
            if (cleanType.isBlank()) return@edit
            val normalized = entity.copy(
                driverName = InputSecurity.cleanName(entity.driverName).ifBlank { "—" },
                type = cleanType,
                durationMinutes = InputSecurity.clampDurationMinutes(entity.durationMinutes),
                rating = InputSecurity.clampRating(entity.rating)
            )
            writeTrainings(prefs, list.map { if (it.id == entity.id) normalized else it })
        }
    }

    suspend fun deleteTraining(id: Long) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val trainings = readTrainings(prefs)
            writeTrainings(prefs, trainings.filterNot { it.id == id })
        }
    }

    suspend fun seedIfEmpty() = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            val driversJson = prefs[GtPreferenceKeys.drivers]
            if (!driversJson.isNullOrBlank() && driversJson != "[]") return@edit

            writeDrivers(
                prefs,
                listOf(
                    DriverEntity(id = 1, name = "Lewis Hamilton", team = "Mercedes", age = 40),
                    DriverEntity(id = 2, name = "Max Verstappen", team = "Red Bull", age = 27),
                    DriverEntity(id = 3, name = "Charles Leclerc", team = "Ferrari", age = 27)
                )
            )
            writeTeams(
                prefs,
                listOf(
                    TeamEntity(id = 1, name = "Mercedes", country = "Németország"),
                    TeamEntity(id = 2, name = "Red Bull", country = "Ausztria"),
                    TeamEntity(id = 3, name = "Ferrari", country = "Olaszország")
                )
            )
            writeRaces(
                prefs,
                listOf(
                    RaceEntity(id = 1, date = "2026-05-14", title = "Nürburgring 24h Quali", location = "Nürburgring"),
                    RaceEntity(id = 2, date = "2026-07-26", title = "Hungaroring GP 2026", location = "Hungaroring"),
                    RaceEntity(id = 3, date = "2026-05-28", title = "Monaco GP", location = "Monte Carlo"),
                    RaceEntity(id = 4, date = "2026-07-05", title = "Silverstone GP", location = "Nagy-Britannia")
                )
            )
            writeTracks(
                prefs,
                listOf(
                    TrackEntity(id = 1, name = "Monza", lengthKm = "5.79", location = "Olaszország"),
                    TrackEntity(id = 2, name = "Spa-Francorchamps", lengthKm = "7.00", location = "Belgium"),
                    TrackEntity(id = 3, name = "Hungaroring", lengthKm = "4.38", location = "Magyarország")
                )
            )
            val now = System.currentTimeMillis()
            writeTrainings(
                prefs,
                listOf(
                    TrainingEntity(id = 1, driverName = "Tóth Bence", type = "Fizikai", durationMinutes = 45, rating = 6, createdAt = now - 86_400_000L),
                    TrainingEntity(id = 2, driverName = "Marco Rossi", type = "Szabadedzés", durationMinutes = 90, rating = 8, createdAt = now - 172_800_000L),
                    TrainingEntity(id = 3, driverName = "—", type = "Szimulátor", durationMinutes = 60, rating = 7, createdAt = now - 2_592_000_000L)
                )
            )
        }
    }

    private fun readDrivers(prefs: Preferences): List<DriverEntity> =
        gson.fromJson(prefs[GtPreferenceKeys.drivers] ?: "[]", driverListType) ?: emptyList()

    private fun writeDrivers(prefs: MutablePreferences, list: List<DriverEntity>) {
        prefs[GtPreferenceKeys.drivers] = gson.toJson(list)
    }

    private fun readTeams(prefs: Preferences): List<TeamEntity> =
        gson.fromJson(prefs[GtPreferenceKeys.teams] ?: "[]", teamListType) ?: emptyList()

    private fun writeTeams(prefs: MutablePreferences, list: List<TeamEntity>) {
        prefs[GtPreferenceKeys.teams] = gson.toJson(list)
    }

    private fun readRaces(prefs: Preferences): List<RaceEntity> =
        gson.fromJson(prefs[GtPreferenceKeys.races] ?: "[]", raceListType) ?: emptyList()

    private fun writeRaces(prefs: MutablePreferences, list: List<RaceEntity>) {
        prefs[GtPreferenceKeys.races] = gson.toJson(list)
    }

    private fun readTracks(prefs: Preferences): List<TrackEntity> =
        gson.fromJson(prefs[GtPreferenceKeys.tracks] ?: "[]", trackListType) ?: emptyList()

    private fun writeTracks(prefs: MutablePreferences, list: List<TrackEntity>) {
        prefs[GtPreferenceKeys.tracks] = gson.toJson(list)
    }

    private fun readTrainings(prefs: Preferences): List<TrainingEntity> =
        gson.fromJson(prefs[GtPreferenceKeys.trainings] ?: "[]", trainingListType) ?: emptyList()

    private fun writeTrainings(prefs: MutablePreferences, list: List<TrainingEntity>) {
        prefs[GtPreferenceKeys.trainings] = gson.toJson(list)
    }
}
