package com.arpitoth.gtracingcompanion.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.security.MessageDigest

enum class UserRole { ADMIN, MEMBER }

data class LocalUser(
    val email: String,
    val passwordHash: String,
    val role: UserRole
)

data class AuthUser(
    val email: String,
    val role: UserRole
)

class AuthRepository(context: Context) {
    private val dataStore = context.gtDataStore
    private val gson = Gson()
    private val userListType = object : TypeToken<List<LocalUser>>() {}.type

    val currentUser: Flow<AuthUser?> = dataStore.data.map { prefs ->
        val email = prefs[GtPreferenceKeys.currentUserEmail] ?: return@map null
        readUsers(prefs).firstOrNull { it.email.equals(email, ignoreCase = true) }?.let {
            AuthUser(email = it.email, role = it.role)
        }
    }

    suspend fun register(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val cleanEmail = InputSecurity.cleanText(email.lowercase(), 120)
        if (!isValidEmail(cleanEmail) || password.length < 6) return@withContext false

        var success = false
        dataStore.edit { prefs ->
            val users = readUsers(prefs)
            if (users.any { it.email.equals(cleanEmail, ignoreCase = true) }) return@edit
            val role = if (users.isEmpty()) UserRole.ADMIN else UserRole.MEMBER
            val newUser = LocalUser(
                email = cleanEmail,
                passwordHash = sha256(password),
                role = role
            )
            writeUsers(prefs, users + newUser)
            prefs[GtPreferenceKeys.currentUserEmail] = cleanEmail
            success = true
        }
        success
    }

    suspend fun login(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val cleanEmail = InputSecurity.cleanText(email.lowercase(), 120)
        if (!isValidEmail(cleanEmail) || password.isBlank()) return@withContext false

        var success = false
        dataStore.edit { prefs ->
            val users = readUsers(prefs)
            val user = users.firstOrNull { it.email.equals(cleanEmail, ignoreCase = true) } ?: return@edit
            if (user.passwordHash == sha256(password)) {
                prefs[GtPreferenceKeys.currentUserEmail] = cleanEmail
                success = true
            }
        }
        success
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            prefs.remove(GtPreferenceKeys.currentUserEmail)
        }
    }

    private fun readUsers(prefs: Preferences): List<LocalUser> =
        gson.fromJson(prefs[GtPreferenceKeys.users] ?: "[]", userListType) ?: emptyList()

    private fun writeUsers(prefs: androidx.datastore.preferences.core.MutablePreferences, users: List<LocalUser>) {
        prefs[GtPreferenceKeys.users] = gson.toJson(users)
    }

    private fun sha256(value: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(value.toByteArray())
        return bytes.joinToString("") { b -> "%02x".format(b) }
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        return regex.matches(email)
    }
}
