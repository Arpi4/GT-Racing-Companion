package com.arpitoth.gtracingcompanion.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class User(val id: Long, val name: String, val age: Int)

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_AGE INTEGER NOT NULL,
                $COLUMN_EMAIL TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_EMAIL TEXT")
        }
    }

    fun getAllUsers(): List<User> {
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, null, null, null, null, null, "$COLUMN_ID ASC")
        val users = mutableListOf<User>()
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val age = it.getInt(it.getColumnIndexOrThrow(COLUMN_AGE))
                // új mező: email
                users.add(User(id, name, age))
            }
        }
        return users
    }

    fun insertUser(name: String, age: Int, email: String? = null) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_AGE, age)
            put(COLUMN_EMAIL, email)
        }
        db.insert(TABLE_USERS, null, values)
    }

    fun updateUser(id: Long, name: String, age: Int, email: String? = null) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_AGE, age)
            put(COLUMN_EMAIL, email)
        }
        db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteUser(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}