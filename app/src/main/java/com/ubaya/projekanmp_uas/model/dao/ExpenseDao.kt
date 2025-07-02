package com.ubaya.projekanmp_uas.model.dao

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ubaya.projekanmp_uas.model.entity.Expense

@Dao
interface ExpenseDao {
    @Insert
    fun insert(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): List<Expense>

    @Query("SELECT * FROM expenses WHERE userId=:userId ORDER BY timestamp DESC")
    fun getUserExpenses(userId:Int): List<Expense>

    @Query("SELECT SUM(amount) FROM expenses WHERE userId= :userId and budgetId = :budgetId")
    fun totalExpenseByBudget(userId: Int,budgetId: Int): Long
}
