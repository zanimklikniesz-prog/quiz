package com.example.quiz_java.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val isGuest: Boolean = false,
    val totalPoints: Int = 0
)
