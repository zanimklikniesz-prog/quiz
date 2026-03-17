package com.example.quiz_java.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.quiz_java.ui.theme.Quiz_JavaTheme

data class CategoryItem(
    val title: String,
    val description: String
)

val quizCategories = listOf(
    CategoryItem("Cybersecurity", "Network Security, Cryptography, and Threats"),
    CategoryItem("Java Basics", "Variables, Data Types, and Operators"),
    CategoryItem("OOP Principles", "Inheritance, Polymorphism, and Encapsulation"),
    CategoryItem("Collections", "Lists, Sets, Maps, and Iterators"),
    CategoryItem("Exception Handling", "Try-Catch, Finally, and Custom Exceptions"),
    CategoryItem("Multithreading", "Threads, Runnables, and Synchronization"),
    CategoryItem("Java I/O", "File Handling, Streams, and Serialization"),
    CategoryItem("Java 8 Features", "Lambdas, Streams API, and Optionals")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryMenuScreen(
    onCategorySelected: (CategoryItem) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Category") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(quizCategories) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: CategoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryMenuScreenPreview() {
    Quiz_JavaTheme {
        CategoryMenuScreen(onCategorySelected = {}, onBack = {})
    }
}
