package com.example.quiz_java.data.dao

import androidx.room.*
import com.example.quiz_java.data.model.Score
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: Score)

    @Query("SELECT * FROM scores WHERE userId = :userId ORDER BY timestamp DESC")
    fun getScoresForUser(userId: Int): Flow<List<Score>>

    @Query("SELECT SUM(points) FROM scores WHERE userId = :userId")
    suspend fun getTotalPointsForUser(userId: Int): Int?
}
