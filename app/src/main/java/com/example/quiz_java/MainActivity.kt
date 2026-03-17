package com.example.quiz_java

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.quiz_java.ui.QuizApp
import com.example.quiz_java.ui.theme.Quiz_JavaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Quiz_JavaTheme {
                QuizApp()
            }
        }
    }
}
