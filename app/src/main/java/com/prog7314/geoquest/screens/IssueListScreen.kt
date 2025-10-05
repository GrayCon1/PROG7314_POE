package com.prog7314.geoquest.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viewmodels.IssueViewModel

@Composable
fun IssueListScreen(viewModel: IssueViewModel = viewModel()) {
    val issues by viewModel.issues

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Reported Issues", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(issues) { issue ->
                Text("${issue.title} - ${issue.status}")
                Divider()
            }
        }
    }
}