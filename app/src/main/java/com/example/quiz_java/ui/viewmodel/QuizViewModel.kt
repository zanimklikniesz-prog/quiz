package com.example.quiz_java.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_java.data.model.Question
import com.example.quiz_java.data.model.Score
import com.example.quiz_java.data.model.User
import com.example.quiz_java.data.repository.QuizRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    var questions by mutableStateOf<List<Question>>(emptyList())
    var currentQuestionIndex by mutableIntStateOf(0)
    var score by mutableIntStateOf(0)
    var selectedOptionIndex by mutableStateOf<Int?>(null)
    var isCorrect by mutableStateOf<Boolean?>(null)
    var isQuizFinished by mutableStateOf(false)
    var currentUserId by mutableIntStateOf(-1)

    val leaderboard: StateFlow<List<User>> = repository.getAllUsersByRanking()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun startQuiz(userId: Int) {
        currentUserId = userId
        viewModelScope.launch {
            // Updated to use all 12 available questions
            questions = repository.getRandomQuestions(12)
            currentQuestionIndex = 0
            score = 0
            isQuizFinished = false
            selectedOptionIndex = null
            isCorrect = null
        }
    }

    fun selectOption(index: Int) {
        if (selectedOptionIndex != null) return

        selectedOptionIndex = index
        val question = questions[currentQuestionIndex]
        isCorrect = index == question.correctOptionIndex
        if (isCorrect == true) {
            score += 10
        }

        viewModelScope.launch {
            delay(1000)
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                selectedOptionIndex = null
                isCorrect = null
            } else {
                isQuizFinished = true
                saveScore()
            }
        }
    }

    private suspend fun saveScore() {
        if (currentUserId != -1) {
            repository.insertScore(Score(userId = currentUserId, points = score))
        }
    }
}
