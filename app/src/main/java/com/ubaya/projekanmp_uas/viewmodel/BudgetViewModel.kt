package com.ubaya.projekanmp_uas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ubaya.projekanmp_uas.model.entity.Budget
import com.ubaya.projekanmp_uas.model.entity.Expense
import com.ubaya.projekanmp_uas.util.buildDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetViewModel (application: Application) : AndroidViewModel(application) {
    val budgetLD = MutableLiveData<Budget>()
    val budgetListLD = MutableLiveData<List<Budget>>()
    val errorLD = MutableLiveData<String>()

    fun selectAll() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = buildDb(getApplication())
                budgetListLD.postValue(db.budgetDao().getAllBudgets())
            } catch (e: Exception) {
                errorLD.postValue("Budget : ${e.message}")
            }
        }
    }
    fun selectUserBudget(userId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = buildDb(getApplication())
                budgetListLD.postValue(db.budgetDao().getUserBudget(userId))
            } catch (e: Exception) {
                errorLD.postValue("Budget : ${e.message}")
            }
        }
    }
    fun addBudget(budget: Budget) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = buildDb(getApplication())
                db.budgetDao().insert(budget)
            } catch (e: Exception) {
                errorLD.postValue("Tambah Budget gagal: ${e.message}")
            }
        }
    }
    fun updateBudget(budget: Budget) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = buildDb(getApplication())
                db.budgetDao().updateBudget(budget.id,budget.iduser,budget.name,budget.maxAmount  )
            } catch (e: Exception) {
                errorLD.postValue("Edit Budget gagal: ${e.message}")
            }
        }
    }
}