package com.example.quiz_java.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz_java.ui.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onBack: () -> Unit,
    onFinish: (Int, Int) -> Unit
) {
    val questions = viewModel.questions
    val currentIndex = viewModel.currentQuestionIndex
    val selectedIndex = viewModel.selectedOptionIndex
    val isCorrect = viewModel.isCorrect
    val score = viewModel.score

    if (viewModel.isQuizFinished) {
        onFinish(score, questions.size)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz - ${currentIndex + 1}/${questions.size}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Text(
                        "Score: $score",
                        modifier = Modifier.padding(end = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ) { innerPadding ->
        if (questions.isNotEmpty() && currentIndex < questions.size) {
            val currentQuestion = questions[currentIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / questions.size },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = currentQuestion.text,
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                currentQuestion.options.forEachIndexed { index, option ->
                    val backgroundColor = when {
                        selectedIndex == index && isCorrect == true -> Color(0xFF4CAF50) // Green
                        selectedIndex == index && isCorrect == false -> Color(0xFFF44336) // Red
                        selectedIndex != null && index == currentQuestion.correctOptionIndex -> Color(0xFF4CAF50) // Show correct
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val contentColor = if (selectedIndex != null && (index == selectedIndex || index == currentQuestion.correctOptionIndex)) {
                        Color.White
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable(enabled = selectedIndex == null) {
                                viewModel.selectOption(index)
                            },
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = backgroundColor,
                            contentColor = contentColor
                        ),
                        border = CardDefaults.outlinedCardBorder(enabled = selectedIndex == null)
                    ) {
                        Text(
                            text = option,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
