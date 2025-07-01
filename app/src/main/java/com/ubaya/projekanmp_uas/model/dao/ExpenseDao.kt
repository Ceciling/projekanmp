package com.ubaya.projekanmp_uas.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ubaya.projekanmp_uas.model.entity.Expense

@Dao
interface ExpenseDao {
//    @Insert
//    suspend fun insert(expense: Expense)
//
//    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
//    fun getAllExpenses(): LiveData<List<Expense>>
//
//    @Query("SELECT SUM(amount) FROM expenses WHERE budgetId = :budgetId")
//    suspend fun totalExpenseByBudget(budgetId: Int): Long
}
