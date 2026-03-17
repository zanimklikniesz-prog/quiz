package com.example.quiz_java

import org.junit.Test
import org.junit.Assert.*

class QuizLogicTest {

    @Test
    fun testQuizScoreCalculation() {
        val totalQuestions = 5
        val correctAnswers = 3
        val pointsPerQuestion = 10
        
        val finalScore = correctAnswers * pointsPerQuestion
        
        assertEquals(30, finalScore)
    }

    @Test
    fun testQuizPassingGrade() {
        val score = 40
        val passingScore = 30
        
        assertTrue("Score should be a passing grade", score >= passingScore)
    }

    @Test
    fun testCategoryListNotEmpty() {
        val categories = listOf("Java Basics", "OOP Principles", "Collections")
        assertFalse("Category list should not be empty", categories.isEmpty())
        assertEquals(3, categories.size)
    }
}
