package com.ubaya.projekanmp_uas.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ubaya.projekanmp_uas.model.entity.User

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUser(username: String): User?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun login(username: String, password: String): User?

    @Query("UPDATE users SET password = :newPassword WHERE username = :username")
    fun updatePassword(username: String, newPassword: String)

}
