package com.zaen.githubuser.db.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zaen.githubuser.db.UserInfoDatabase
import com.zaen.githubuser.db.util.DatabaseContract.UserInfoColumns.Companion.ID
import com.zaen.githubuser.db.util.DatabaseContract.UserInfoColumns.Companion.TABLE_NAME


class UserHelper(context: Context) {
    private val userInfoDatabase: UserInfoDatabase = UserInfoDatabase(context)

    private lateinit var database: SupportSQLiteDatabase

    companion object {
        private var INSTANCE: UserHelper? = null

        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }

    private fun queryBuilder() : SQLiteQueryBuilder {
        val builder = SQLiteQueryBuilder()
        builder.tables = TABLE_NAME
        return builder
    }

    @Throws(SQLException::class)
    fun open() {
        database = userInfoDatabase.openHelper.writableDatabase
    }

    fun close() {
        userInfoDatabase.close()

        if (database.isOpen)
            database.close()
    }

    /**
     * Ambil data dari semua note yang ada di dalam database
     * @return cursor hasil queryAll
     */
    fun queryAll(): Cursor {
        val builder = queryBuilder()
        val query = builder.buildQuery(
            null,
            null,
            null,
            null,
            "$ID DESC",
            null)

        return database.query(query)
    }

    fun queryById(id: String): Cursor {
        val builder = queryBuilder()
        val query = builder.buildQuery(
            null,
            "$ID = ${arrayOf(id)}",
            null,
            null,
            "$ID DESC",
            null)

        return database.query(query)
    }

    @Throws(SQLException::class)
    fun insert(values: ContentValues?): Long {
        return database.insert(TABLE_NAME, OnConflictStrategy.REPLACE, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(TABLE_NAME, OnConflictStrategy.REPLACE, values, "$ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(TABLE_NAME, "$ID = '$id'", null)
    }
}