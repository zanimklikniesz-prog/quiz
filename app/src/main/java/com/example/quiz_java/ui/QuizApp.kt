package com.example.quiz_java.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.quiz_java.data.database.QuizDatabase
import com.example.quiz_java.data.model.User
import com.example.quiz_java.data.repository.QuizRepository
import com.example.quiz_java.ui.screens.HomeScreen
import com.example.quiz_java.ui.screens.QuizScreen
import com.example.quiz_java.ui.screens.WelcomeScreen
import com.example.quiz_java.ui.screens.ResultScreen
import com.example.quiz_java.ui.screens.LeaderboardScreen
import com.example.quiz_java.ui.viewmodel.QuizViewModel
import com.example.quiz_java.ui.viewmodel.QuizViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun QuizApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = QuizDatabase.getDatabase(context)
    val repository = QuizRepository(database.userDao(), database.questionDao(), database.scoreDao())
    val viewModel: QuizViewModel = viewModel(factory = QuizViewModelFactory(repository))
    val scope = rememberCoroutineScope()
    
    var currentUser by remember { mutableStateOf<User?>(null) }

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                onRegister = { username ->
                    scope.launch {
                        val existingUser = repository.getUserByUsername(username)
                        if (existingUser != null) {
                            currentUser = existingUser
                        } else {
                            val newUser = User(username = username)
                            val id = repository.insertUser(newUser)
                            currentUser = newUser.copy(id = id.toInt())
                        }
                        navController.navigate("home")
                    }
                },
                onGuestLogin = {
                    scope.launch {
                        val guestUser = User(username = "Guest", isGuest = true)
                        val id = repository.insertUser(guestUser)
                        currentUser = guestUser.copy(id = id.toInt())
                        navController.navigate("home")
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                username = currentUser?.username ?: "Guest",
                onStartQuiz = {
                    currentUser?.let { user ->
                        viewModel.startQuiz(user.id)
                        navController.navigate("quiz")
                    }
                },
                onViewLeaderboard = {
                    navController.navigate("leaderboard")
                },
                onLogout = {
                    currentUser = null
                    navController.navigate("welcome") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }
        composable("quiz") {
            QuizScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onFinish = { score, total ->
                    navController.navigate("result/$score/$total") {
                        popUpTo("home")
                    }
                }
            )
        }
        composable("leaderboard") {
            LeaderboardScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "result/{score}/{total}",
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0
            ResultScreen(
                score = score,
                totalQuestions = total,
                onPlayAgain = {
                    currentUser?.let { user ->
                        viewModel.startQuiz(user.id)
                        navController.navigate("quiz") {
                            popUpTo("home")
                        }
                    }
                },
                onGoHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
