package com.prog7314.geoquest.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.prog7314.geoquest.data.data.LocationData
import kotlinx.coroutines.tasks.await

class LocationRepo {

    private val firestore = FirebaseFirestore.getInstance()
    private val locationsCollection = firestore.collection("locations")

    suspend fun addLocation(locationData: LocationData): Result<String> {
        return try {
            val docRef = locationsCollection.document()
            val locationWithId = locationData.copy(id = docRef.id)
            docRef.set(locationWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteLocation(locationId: String): Result<Unit> {
        return try {
            locationsCollection.document(locationId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllLocations(): Result<List<LocationData>> {
        return try {
            val snapshot = locationsCollection
                .orderBy("dateAdded", Query.Direction.DESCENDING)
                .get()
                .await()
            val locations = snapshot.documents.mapNotNull { doc ->
                doc.toObject(LocationData::class.java)?.copy(id = doc.id)
            }
            Result.success(locations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserLocations(userId: String): Result<List<LocationData>> {
        return try {
            val snapshot = locationsCollection
                .whereEqualTo("userId", userId)
                .orderBy("dateAdded", Query.Direction.DESCENDING)
                .get()
                .await()
            val locations = snapshot.documents.mapNotNull { doc ->
                doc.toObject(LocationData::class.java)?.copy(id = doc.id)
            }
            Result.success(locations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
