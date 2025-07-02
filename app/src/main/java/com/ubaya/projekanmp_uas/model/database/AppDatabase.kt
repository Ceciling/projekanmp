package com.ubaya.projekanmp_uas.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ubaya.projekanmp_uas.model.dao.BudgetDao
import com.ubaya.projekanmp_uas.model.dao.ExpenseDao
import com.ubaya.projekanmp_uas.model.dao.UserDao
import com.ubaya.projekanmp_uas.model.entity.Budget
import com.ubaya.projekanmp_uas.model.entity.Expense
import com.ubaya.projekanmp_uas.model.entity.User
import com.ubaya.projekanmp_uas.util.DB_NAME
import com.ubaya.projekanmp_uas.util.MIGRATION_1_2

@Database(entities = [User::class, Budget::class, Expense::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun budgetDao(): BudgetDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(LOCK) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )

                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { instance = it }
            }

        operator fun invoke(context: Context) {
            if (instance == null) {
                synchronized(LOCK) {
                    instance ?: getDatabase(context).also {
                        instance = it
                    }
                }
            }
        }
    }
}

