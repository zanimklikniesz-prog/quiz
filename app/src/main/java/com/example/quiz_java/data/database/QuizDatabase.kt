package com.example.quiz_java.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.quiz_java.data.dao.QuestionDao
import com.example.quiz_java.data.dao.ScoreDao
import com.example.quiz_java.data.dao.UserDao
import com.example.quiz_java.data.model.Question
import com.example.quiz_java.data.model.Score
import com.example.quiz_java.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Question::class, Score::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        fun getDatabase(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                populateDatabase(database.questionDao())
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateDatabase(questionDao: QuestionDao) {
            val sampleQuestions = listOf(
                Question(
                    text = "What is the capital of France?",
                    options = listOf("London", "Berlin", "Paris", "Madrid"),
                    correctOptionIndex = 2,
                    category = "General"
                ),
                Question(
                    text = "Which planet is known as the Red Planet?",
                    options = listOf("Venus", "Mars", "Jupiter", "Saturn"),
                    correctOptionIndex = 1,
                    category = "Science"
                ),
                Question(
                    text = "What is the largest mammal in the world?",
                    options = listOf("Elephant", "Blue Whale", "Giraffe", "Hippopotamus"),
                    correctOptionIndex = 1,
                    category = "Nature"
                ),
                Question(
                    text = "Who wrote 'Romeo and Juliet'?",
                    options = listOf("Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain"),
                    correctOptionIndex = 1,
                    category = "Literature"
                ),
                Question(
                    text = "What is the chemical symbol for gold?",
                    options = listOf("Gd", "Go", "Ag", "Au"),
                    correctOptionIndex = 3,
                    category = "Science"
                )
            )
            questionDao.insertQuestions(sampleQuestions)
        }
    }
}
