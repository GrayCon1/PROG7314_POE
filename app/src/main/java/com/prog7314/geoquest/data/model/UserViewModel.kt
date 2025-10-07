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
    fun getCurrentUser(): UserData? {
        return _currentUser.value
    }

    fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }
    fun updateUser(userId: String, name: String, email: String, newPassword: String?, currentPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // First verify current password
            _currentUser.value?.let { user ->
                if (user.password != currentPassword) {
                    _errorMessage.value = "Current password is incorrect"
                    _isLoading.value = false
                    return@launch
                }

                // Check if any changes were made
                val hasNameChanged = name.trim() != user.name
                val hasEmailChanged = email.trim().lowercase() != user.email
                val hasPasswordChanged = !newPassword.isNullOrBlank()

                if (!hasNameChanged && !hasEmailChanged && !hasPasswordChanged) {
                    _errorMessage.value = "No changes were made"
                    _isLoading.value = false
                    return@launch
                }

                // If email changed, check if new email already exists
                if (hasEmailChanged) {
                    userRepo.checkUserExists(email.trim().lowercase())
                        .onSuccess { exists ->
                            if (exists) {
                                _errorMessage.value = "Email already exists"
                                _isLoading.value = false
                                return@onSuccess
                            }

                            // Proceed with update
                            performUpdate(userId, name, email, newPassword, user)
                        }
                        .onFailure { exception ->
                            _errorMessage.value = exception.message
                            _isLoading.value = false
                        }
                } else {
                    // No email change, proceed with update
                    performUpdate(userId, name, email, newPassword, user)
                }
            }
        }
    }

    private suspend fun performUpdate(userId: String, name: String, email: String, newPassword: String?, currentUser: UserData) {
        val updatedUser = currentUser.copy(
            name = name.trim(),
            email = email.trim().lowercase(),
            password = if (!newPassword.isNullOrBlank()) newPassword else currentUser.password
        )

        userRepo.updateUser(userId, updatedUser)
            .onSuccess {
                _currentUser.value = updatedUser
                _errorMessage.value = "Profile updated successfully"
            }
            .onFailure { exception ->
                _errorMessage.value = exception.message
            }
        _isLoading.value = false
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
