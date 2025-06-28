package com.ubaya.projekanmp_uas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ubaya.projekanmp_uas.model.database.AppDatabase.Companion.getDatabase
import com.ubaya.projekanmp_uas.model.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()

    val userLD = MutableLiveData<User?>()
    val registerSuccessLD = MutableLiveData<Boolean>()
    val errorLD = MutableLiveData<String>()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun login(username: String, password: String) {
        launch {
            try {
                val db = getDatabase(getApplication())
                val user = db.userDao().login(username, password)
                userLD.postValue(user)
            } catch (e: Exception) {
                errorLD.postValue("Login gagal: ${e.message}")
            }
        }
    }

    fun register(user: User) {
        launch {
            try {
                val db = getDatabase(getApplication())
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
}