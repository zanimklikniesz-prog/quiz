package com.example.quiz_java.data.dao

import androidx.room.*
import com.example.quiz_java.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users ORDER BY totalPoints DESC")
    fun getAllUsersByRanking(): Flow<List<User>>

    @Delete
    suspend fun deleteUser(user: User)
}
