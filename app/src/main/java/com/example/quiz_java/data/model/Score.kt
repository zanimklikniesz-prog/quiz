package com.example.quiz_java.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "scores",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val points: Int,
    val timestamp: Long = System.currentTimeMillis()
)
