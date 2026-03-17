package com.example.quiz_java.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val category: String
)
