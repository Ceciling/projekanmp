package com.ubaya.projekanmp_uas.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ubaya.projekanmp_uas.model.entity.Budget

@Dao
interface BudgetDao {
    @Insert
    fun insert(budget: Budget)

    @Update
    fun update(budget: Budget)

    @Query("SELECT * FROM budgets ORDER BY createdAt DESC")
    fun getAllBudgets(): LiveData<List<Budget>>
}
