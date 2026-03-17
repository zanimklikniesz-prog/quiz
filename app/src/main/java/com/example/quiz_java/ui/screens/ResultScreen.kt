package com.example.quiz_java.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.quiz_java.ui.theme.Quiz_JavaTheme

@Composable
fun ResultScreen(
    score: Int,
    totalQuestions: Int,
    onPlayAgain: () -> Unit,
    onGoHome: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Quiz Finished!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your Score",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "$score / ${totalQuestions * 10}",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            val performanceMessage = when {
                score >= totalQuestions * 8 -> "Excellent! You're a pro!"
                score >= totalQuestions * 5 -> "Good job! Keep it up!"
                else -> "Keep practicing! You'll get better."
            }

            Text(
                text = performanceMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Play Again", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onGoHome,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Go to Home", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    Quiz_JavaTheme {
        ResultScreen(score = 40, totalQuestions = 5, onPlayAgain = {}, onGoHome = {})
    }
}
