package com.ubaya.projekanmp_uas.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val maxAmount: Long,
    val createdAt: Long = System.currentTimeMillis() / 1000
)
