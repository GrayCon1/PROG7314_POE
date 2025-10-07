package com.prog7314.geoquest.data.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prog7314.geoquest.data.data.UserData
import com.prog7314.geoquest.data.repo.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val userRepo = UserRepo()

    // UI State
    private val _currentUser = MutableStateFlow<UserData?>(null)
    val currentUser: StateFlow<UserData?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    // Add new user
    fun registerUser(userData: UserData) {
        viewModelScope.launch {
            _isLoading.value = true
            // First check if user already exists
            userRepo.checkUserExists(userData.email)
                .onSuccess { exists ->
                    if (exists) {
                        _errorMessage.value = "User with this email already exists"
                        _isLoading.value = false
                    } else {
                        // User doesn't exist, proceed with registration
                        userRepo.addUser(userData)
                            .onSuccess { userId ->
                                _currentUser.value = userData.copy(id = userId)
                                _errorMessage.value = null
                            }
                            .onFailure { exception ->
                                _errorMessage.value = exception.message
                            }
                        _isLoading.value = false
                    }
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                    _isLoading.value = false
                }
        }
    }

    // Login user with email and password
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            userRepo.getUserByEmailAndPassword(email, password)
                .onSuccess { user ->
                    if (user != null) {
                        _currentUser.value = user
                        _loginSuccess.value = true
                        _errorMessage.value = null
                    } else {
                        _errorMessage.value = "Invalid email or password"
                        _loginSuccess.value = false
                    }
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                    _loginSuccess.value = false
                }
            _isLoading.value = false
        }
    }

    // Delete user
    fun deleteUser(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            userRepo.deleteUser(userId)
                .onSuccess {
                    _currentUser.value = null
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            _isLoading.value = false
        }
    }

    // Logout user
    fun logoutUser() {
        _currentUser.value = null
        _loginSuccess.value = false
    }

    // Clear error message
    fun clearError() {
        _errorMessage.value = null
    }
}
