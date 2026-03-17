package com.example.quiz_java.data.repository

import com.example.quiz_java.data.dao.QuestionDao
import com.example.quiz_java.data.dao.ScoreDao
import com.example.quiz_java.data.dao.UserDao
import com.example.quiz_java.data.model.Question
import com.example.quiz_java.data.model.Score
import com.example.quiz_java.data.model.User
import kotlinx.coroutines.flow.Flow

class QuizRepository(
    private val userDao: UserDao,
    private val questionDao: QuestionDao,
    private val scoreDao: ScoreDao
) {
    // User operations
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)
    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)
    fun getAllUsersByRanking(): Flow<List<User>> = userDao.getAllUsersByRanking()

    // Question operations
    suspend fun insertQuestions(questions: List<Question>) = questionDao.insertQuestions(questions)
    fun getAllQuestions(): Flow<List<Question>> = questionDao.getAllQuestions()
    fun getQuestionsByCategory(category: String): Flow<List<Question>> = questionDao.getQuestionsByCategory(category)
    suspend fun getRandomQuestions(limit: Int): List<Question> = questionDao.getRandomQuestions(limit)

    // Score operations
    suspend fun insertScore(score: Score) {
        scoreDao.insertScore(score)
        // Update user's total points
        val user = userDao.getUserById(score.userId)
        if (user != null) {
            val totalPoints = scoreDao.getTotalPointsForUser(score.userId) ?: 0
            userDao.updateUser(user.copy(totalPoints = totalPoints))
        }
    }
    fun getScoresForUser(userId: Int): Flow<List<Score>> = scoreDao.getScoresForUser(userId)
}
