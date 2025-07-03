package com.ubaya.projekanmp_uas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ubaya.projekanmp_uas.model.entity.Expense
import com.ubaya.projekanmp_uas.model.entity.User
import com.ubaya.projekanmp_uas.util.buildDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel (application: Application) : AndroidViewModel(application) {
    val expenseLD = MutableLiveData<Expense>()
    val amountLD = MutableLiveData<Long>()
    val expenseListLD = MutableLiveData<List<Expense>>()
    val errorLD = MutableLiveData<String>()

    fun selectAll() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = buildDb(getApplication())
                expenseListLD.postValue(db.expenseDao().getAllExpenses())
            } catch (e: Exception) {
                errorLD.postValue("Expense: ${e.message}")
            }
        }
    }
    fun selectUserExpense(userId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = buildDb(getApplication())
                expenseListLD.postValue(db.expenseDao().getUserExpenses(userId))
            } catch (e: Exception) {
                errorLD.postValue("Expense: ${e.message}")
            }
        }
    }
    fun totalExpenseByBudget(userId:Int,budgetId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = buildDb(getApplication())
                amountLD.postValue(db.expenseDao().totalExpenseByBudget(userId,budgetId))
            } catch (e: Exception) {
                errorLD.postValue("Expense: ${e.message}")
            }
        }
    }
    fun addExpenses(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = buildDb(getApplication())
                db.expenseDao().insert(expense)
            } catch (e: Exception) {
                errorLD.postValue("Registrasi gagal: ${e.message}")
            }
        }
    }

    suspend fun getTotalByBudget(userId: Int, budgetId: Int): Long {
        val db = buildDb(getApplication())
        return db.expenseDao().totalExpenseByBudget(userId, budgetId)
    }

//    fun login(username: String, password: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val db = buildDb(getApplication())
//                val user = db.userDao().login(username, password)
//                userLD.postValue(user)
//            } catch (e: Exception) {
//                errorLD.postValue("Login gagal: ${e.message}")
//            }
//        }
//    }
}