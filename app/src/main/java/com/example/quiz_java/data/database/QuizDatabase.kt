package com.example.quiz_java.data.database

import android.content.Context
import android.util.Log
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

@Database(entities = [User::class, Question::class, Score::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    abstract fun scoreDao(): ScoreDao

    companion object {
        private const val TAG = "QuizDatabase"
        
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        fun getDatabase(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                )
                .fallbackToDestructiveMigration(true)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d(TAG, "Database onCreate - Queuing population")
                    }
                    
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        Log.d(TAG, "Database onOpen - Checking for questions")
                        // Ensure data is present on every open if empty
                        CoroutineScope(Dispatchers.IO).launch {
                            checkAndPopulate(getDatabase(context))
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun checkAndPopulate(database: QuizDatabase) {
            try {
                val questionDao = database.questionDao()
                val count = database.query("SELECT COUNT(*) FROM questions", null).use {
                    if (it.moveToFirst()) it.getInt(0) else 0
                }
                
                Log.d(TAG, "Current question count: $count")
                
                if (count < 12) {
                    Log.d(TAG, "Populating IT Security questions pool...")
                    questionDao.deleteAllQuestions()
                    populateDatabase(questionDao)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error checking/populating database", e)
            }
        }

        private suspend fun populateDatabase(questionDao: QuestionDao) {
            val itSecurityQuestions = listOf(
                // --- BASIC ---
                Question(
                    text = "What makes a password 'strong'?",
                    options = listOf("Your name and birth year", "The word 'password123'", "A mix of letters, numbers, and symbols", "A single short word"),
                    correctOptionIndex = 2,
                    category = "Basic Security"
                ),
                Question(
                    text = "Why is it important to keep your software and apps updated?",
                    options = listOf("To use more storage space", "Updates often fix security vulnerabilities", "To change the app icon", "It is not important"),
                    correctOptionIndex = 1,
                    category = "Basic Security"
                ),
                Question(
                    text = "Which of these is a sign of a secure website in the browser?",
                    options = listOf("A bright red background", "A 'Locked' padlock icon in the address bar", "Lots of pop-up ads", "A 'Not Secure' warning"),
                    correctOptionIndex = 1,
                    category = "Basic Security"
                ),
                Question(
                    text = "What does 'Phishing' usually involve?",
                    options = listOf("Catching fish online", "Fake emails or websites to steal info", "Improving internet speed", "A type of computer hardware"),
                    correctOptionIndex = 1,
                    category = "Basic Security"
                ),

                // --- INTERMEDIATE ---
                Question(
                    text = "What is the most secure way to handle your passwords?",
                    options = listOf("Write them on a sticky note", "Use the same password for all accounts", "Use a password manager", "Share them with friends"),
                    correctOptionIndex = 2,
                    category = "Intermediate Security"
                ),
                Question(
                    text = "What is 'Two-Factor Authentication' (2FA)?",
                    options = listOf("Logging in twice", "Using two computers", "Adding a second step to verify your identity", "Using two different browsers"),
                    correctOptionIndex = 2,
                    category = "Intermediate Security"
                ),
                Question(
                    text = "What is the primary risk of using public Wi-Fi for sensitive tasks like banking?",
                    options = listOf("It is too slow", "Your data can be intercepted by hackers", "It costs too much money", "It drains your battery faster"),
                    correctOptionIndex = 1,
                    category = "Intermediate Security"
                ),
                Question(
                    text = "What is 'Malware'?",
                    options = listOf("A type of computer hardware", "Software designed to damage or gain unauthorized access", "A popular social media app", "An internet service provider"),
                    correctOptionIndex = 1,
                    category = "Intermediate Security"
                ),

                // --- ADVANCED ---
                Question(
                    text = "What is 'Ransomware'?",
                    options = listOf("Software that helps you find deals", "Malware that locks your files and demands payment", "A type of computer memory", "A tool for fixing slow computers"),
                    correctOptionIndex = 1,
                    category = "Advanced Security"
                ),
                Question(
                    text = "What is the safest way to dispose of an old computer or phone?",
                    options = listOf("Just throw it in the trash", "Give it away without doing anything", "Securely wipe all data first", "Delete the desktop icons"),
                    correctOptionIndex = 2,
                    category = "Advanced Security"
                ),
                Question(
                    text = "What should you check before entering credentials on a login page?",
                    options = listOf("The color of the page", "The website URL for correctness", "The number of images on the page", "If the page has music"),
                    correctOptionIndex = 1,
                    category = "Advanced Security"
                ),
                Question(
                    text = "What should you do if you receive an unexpected link from a known contact?",
                    options = listOf("Click it immediately", "Verify with the sender through another channel", "Delete the message", "Forward it to others"),
                    correctOptionIndex = 1,
                    category = "Advanced Security"
                )
            )
            questionDao.insertQuestions(itSecurityQuestions)
            Log.d(TAG, "Successfully populated DB with ${itSecurityQuestions.size} IT Security questions.")
        }
    }
}
