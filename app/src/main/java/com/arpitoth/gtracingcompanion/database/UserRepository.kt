package com.arpitoth.gtracingcompanion.repository

import android.content.Context
import com.arpitoth.gtracingcompanion.database.MyDatabaseHelper
import com.arpitoth.gtracingcompanion.database.User

class UserRepository(context: Context) {

    private val dbHelper = MyDatabaseHelper(context)

    fun getAllUsers(): List<User> {
        return dbHelper.getAllUsers()
    }

    fun addUser(name: String, age: Int, email: String? = null) {
        dbHelper.insertUser(name, age, email)
    }

    fun updateUser(user: User, email: String? = null) {
        dbHelper.updateUser(user.id, user.name, user.age, email)
    }

    fun deleteUser(userId: Long) {
        dbHelper.deleteUser(userId)
    }
}