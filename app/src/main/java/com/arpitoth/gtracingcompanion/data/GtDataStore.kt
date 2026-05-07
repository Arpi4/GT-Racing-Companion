package com.arpitoth.gtracingcompanion.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.gtDataStore: DataStore<Preferences> by preferencesDataStore(name = "gt_racing_companion")

object GtPreferenceKeys {
    val drivers = stringPreferencesKey("drivers_json")
    val teams = stringPreferencesKey("teams_json")
    val races = stringPreferencesKey("races_json")
    val tracks = stringPreferencesKey("tracks_json")
    val trainings = stringPreferencesKey("trainings_json")
    val users = stringPreferencesKey("users_json")
    val currentUserEmail = stringPreferencesKey("current_user_email")
}
