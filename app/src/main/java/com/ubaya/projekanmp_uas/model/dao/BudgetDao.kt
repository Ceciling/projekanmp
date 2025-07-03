package com.ubaya.projekanmp_uas.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ubaya.projekanmp_uas.model.entity.Budget
import com.ubaya.projekanmp_uas.model.entity.Expense

@Dao
interface BudgetDao {
    @Insert
    fun insert(budget: Budget)

    @Update
    fun update(budget: Budget)

    @Query("UPDATE budgets SET name=:nama, maxAmount=:budget WHERE id= :id and idUser=:userid")
    fun updateBudget(id:Int,userid:Int,nama:String,budget:Long)

    @Query("SELECT * FROM budgets ORDER BY createdAt DESC")
    fun getAllBudgets(): List<Budget>

    @Query("SELECT * FROM budgets WHERE iduser=:userId")
    fun getUserBudget(userId:Int): List<Budget>

    @Query("SELECT * FROM budgets WHERE id = :id")
    fun getBudgetById(id: Int): Budget
}
