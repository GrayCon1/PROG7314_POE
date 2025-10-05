package com.example.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.models.Issue
import com.example.network.ApiClient
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class IssueViewModel : ViewModel() {
    private val _issues = mutableStateOf<List<Issue>>(emptyList())
    val issues: State<List<Issue>> = _issues

    init {
        viewModelScope.launch {
            try {
                _issues.value = ApiClient.retrofit.getIssues()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}