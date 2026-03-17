package com.example.quiz_java.data.dao

import androidx.room.*
import com.example.quiz_java.data.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE category = :category")
    fun getQuestionsByCategory(category: String): Flow<List<Question>>

    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestions(limit: Int): List<Question>

    @Query("SELECT * FROM questions WHERE category = :category ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestionsByCategory(category: String, limit: Int): List<Question>

    @Query("DELETE FROM questions")
    suspend fun deleteAllQuestions()
}
