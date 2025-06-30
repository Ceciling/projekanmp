package com.ubaya.projekanmp_uas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ubaya.projekanmp_uas.model.database.AppDatabase.Companion.getDatabase
import com.ubaya.projekanmp_uas.model.entity.User
import com.ubaya.projekanmp_uas.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application) {
    val userLD = MutableLiveData<User?>()
    val registerSuccessLD = MutableLiveData<Boolean>()
    val errorLD = MutableLiveData<String>()

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = buildDb(getApplication())
                val user = db.userDao().login(username, password)
                userLD.postValue(user)
            } catch (e: Exception) {
                errorLD.postValue("Login gagal: ${e.message}")
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = buildDb(getApplication())
                val existingUser = db.userDao().getUser(user.username)
                if (existingUser == null) {
                    db.userDao().insertUser(user)
                    registerSuccessLD.postValue(true)
                } else {
                    errorLD.postValue("Username sudah terdaftar")
                    registerSuccessLD.postValue(false)
                }
            } catch (e: Exception) {
                errorLD.postValue("Registrasi gagal: ${e.message}")
                registerSuccessLD.postValue(false)
            }
        }
    }

    fun validatePassword(username: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = buildDb(getApplication())
            val user = db.userDao().login(username, password)
            withContext(Dispatchers.Main) {
                callback(user != null)
            }
        }
    }

    fun updatePassword(username: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = buildDb(getApplication())
            db.userDao().updatePassword(username, newPassword)
        }
    }
}