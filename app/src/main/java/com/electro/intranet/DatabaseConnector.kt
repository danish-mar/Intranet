package com.electro.intranet

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseConnector(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "intranet_db"
        const val DATABASE_VERSION = 1

        // Define table and column names
        const val TABLE_HISTORY = "history"
        const val COLUMN_ID = "_id"
        const val COLUMN_URL = "url"
        const val COLUMN_TITLE = "title"
        const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create the tables when the database is first created
        val createHistoryTableSQL = """
            CREATE TABLE $TABLE_HISTORY (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_URL TEXT,
                $COLUMN_TITLE TEXT,
                $COLUMN_TIMESTAMP INTEGER
            )
        """.trimIndent()

        db?.execSQL(createHistoryTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Upgrade database schema if needed (e.g., alter table or drop table)
    }

    // Method to add a history entry to the database
    fun addHistoryEntry(url: String, title: String, timestamp: Long) {
        val values = ContentValues().apply {
            put(COLUMN_URL, url)
            put(COLUMN_TITLE, title)
            put(COLUMN_TIMESTAMP, timestamp)
        }

        writableDatabase.insert(TABLE_HISTORY, null, values)
    }

    // Method to retrieve all history entries from the database
    fun getAllHistoryEntries(): List<HistoryEntry> {
        val historyEntries = mutableListOf<HistoryEntry>()
        val cursor = readableDatabase.query(
            TABLE_HISTORY,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val url = it.getString(it.getColumnIndexOrThrow(COLUMN_URL))
                val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                val timestamp = it.getLong(it.getColumnIndexOrThrow(COLUMN_TIMESTAMP))

                val historyEntry = HistoryEntry(id, url, title, timestamp)
                historyEntries.add(historyEntry)
            }
        }

        return historyEntries
    }

    // Method to delete a history entry from the database
    fun deleteHistoryEntry(id: Long) {
        writableDatabase.delete(TABLE_HISTORY, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun getLatestHistoryEntry(): HistoryEntry? {
        val cursor = readableDatabase.query(
            TABLE_HISTORY,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_TIMESTAMP DESC",
            "1"
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
            HistoryEntry(id, url, title, timestamp)
        } else {
            null
        }
    }


}
