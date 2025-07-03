package com.ubaya.projekanmp_uas.util

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ubaya.projekanmp_uas.model.database.AppDatabase

val DB_NAME = "finance_db"
fun buildDb(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "budgetdb")
        .fallbackToDestructiveMigration()
        .build()
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE expenses ADD COLUMN priority INTEGER DEFAULT 3 NOT NULL")
    }
}

