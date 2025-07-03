package com.ubaya.projekanmp_uas.util

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ubaya.projekanmp_uas.model.database.AppDatabase

val DB_NAME = "finance_db"
fun buildDb(context: Context): AppDatabase {
    return AppDatabase.getDatabase(context)
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE expense ADD COLUMN priority INTEGER DEFAULT 3 NOT NULL")
    }
}

