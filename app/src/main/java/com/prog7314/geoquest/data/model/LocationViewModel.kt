package com.prog7314.geoquest.data.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prog7314.geoquest.data.data.LocationData
import com.prog7314.geoquest.data.repo.LocationRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private val locationRepo = LocationRepo()

    // UI State
    private val _locations = MutableStateFlow<List<LocationData>>(emptyList())
    val locations: StateFlow<List<LocationData>> = _locations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Load all locations
    fun loadAllLocations() {
        viewModelScope.launch {
            _isLoading.value = true
            locationRepo.getAllLocations()
                .onSuccess { locationList ->
                    _locations.value = locationList
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            _isLoading.value = false
        }
    }

    // Load user-specific locations
    fun loadUserLocations(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            locationRepo.getUserLocations(userId)
                .onSuccess { locationList ->
                    _locations.value = locationList
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            _isLoading.value = false
        }
    }

    // Add new location
    fun addLocation(locationData: LocationData) {
        viewModelScope.launch {
            _isLoading.value = true
            locationRepo.addLocation(locationData)
                .onSuccess { locationId ->
                    // Refresh locations after adding
                    loadAllLocations()
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            _isLoading.value = false
        }
    }

    // Delete location
    fun deleteLocation(locationId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            locationRepo.deleteLocation(locationId)
                .onSuccess {
                    // Refresh locations after deletion
                    loadAllLocations()
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun loadUserLocationsByDateRange(userId: String, startDate: Long, endDate: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            locationRepo.getUserLocationsByDateRange(userId, startDate, endDate)
                .onSuccess { locationList ->
                    _locations.value = locationList
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            _isLoading.value = false
        }
    }

    // Load user locations by date range and visibility
    fun loadUserLocationsByDateRangeAndVisibility(
        userId: String,
        startDate: Long,
        endDate: Long,
        visibility: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            locationRepo.getUserLocationsByDateRangeAndVisibility(
                userId,
                startDate,
                endDate,
                visibility
            )
                .onSuccess { locationList ->
                    _locations.value = locationList
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            _isLoading.value = false
        }
    }

    // Load public locations by date range
    fun loadPublicLocationsByDateRange(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            locationRepo.getPublicLocationsByDateRange(startDate, endDate)
                .onSuccess { locationList ->
                    _locations.value = locationList
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            _isLoading.value = false
        }
    }

    // Clear error message
    fun clearError() {
        _errorMessage.value = null
    }
}
