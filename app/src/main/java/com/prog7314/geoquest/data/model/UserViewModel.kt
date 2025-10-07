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

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            userRepo.signInWithGoogle(idToken)
                .onSuccess { user ->
                    _currentUser.value = user
                    _loginSuccess.value = true
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = "Google Sign-In failed: ${exception.message}"
                    _loginSuccess.value = false
                }
            _isLoading.value = false
        }
    }

    // ... rest of UserViewModel
    fun registerUser(userData: UserData, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            userRepo.registerUser(userData, password)
                .onSuccess { registeredUser ->
                    _currentUser.value = registeredUser
                    _loginSuccess.value = true
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            userRepo.loginUser(email, password)
                .onSuccess { user ->
                    _currentUser.value = user
                    _loginSuccess.value = true
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                    _loginSuccess.value = false
                }
            _isLoading.value = false
        }
    }

    fun updateUser(userData: UserData, currentPassword: String?, newPassword: String?) {
        viewModelScope.launch {
            _isLoading.value = true

            // Update profile
            userRepo.updateUserProfile(userData)
                .onSuccess { updatedUser ->
                    _currentUser.value = updatedUser

                    // Update password if provided
                    if (!currentPassword.isNullOrBlank() && !newPassword.isNullOrBlank()) {
                        userRepo.updatePassword(currentPassword, newPassword)
                    }

                    _errorMessage.value = "Profile updated successfully"
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun logoutUser() {
        userRepo.logoutUser()
        _currentUser.value = null
        _loginSuccess.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
