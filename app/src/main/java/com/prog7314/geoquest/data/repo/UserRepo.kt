package com.prog7314.geoquest.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.prog7314.geoquest.data.data.UserData
import kotlinx.coroutines.tasks.await

class UserRepo {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun registerUser(userData: UserData, password: String): Result<UserData> {
        return try {
            // Create user in Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(userData.email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("User creation failed")

            // Update display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(userData.name)
                .build()
            authResult.user?.updateProfile(profileUpdates)?.await()

            // Create user profile in Firestore with Auth UID
            val userDataWithId = userData.copy(id = userId)
            usersCollection.document(userId).set(userDataWithId).await()

            Result.success(userDataWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<UserData> {
        return try {
            // Sign in with Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Login failed")

            // Get user profile from Firestore
            val doc = usersCollection.document(userId).get().await()
            val userData = doc.toObject(UserData::class.java)?.copy(id = userId)
                ?: throw Exception("User profile not found")

            Result.success(userData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(userData: UserData): Result<UserData> {
        return try {
            // Update Firestore document
            usersCollection.document(userData.id).set(userData).await()

            // Update display name in Firebase Auth
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(userData.name)
                .build()
            auth.currentUser?.updateProfile(profileUpdates)?.await()

            Result.success(userData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit> {
        return try {
            val user = auth.currentUser ?: throw Exception("No user logged in")
            val email = user.email ?: throw Exception("Email not found")

            // Re-authenticate before password change
            val credential = com.google.firebase.auth.EmailAuthProvider
                .getCredential(email, currentPassword)
            user.reauthenticate(credential).await()

            // Update password
            user.updatePassword(newPassword).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(userId: String): Result<UserData> {
        return try {
            val doc = usersCollection.document(userId).get().await()
            val userData = doc.toObject(UserData::class.java)?.copy(id = userId)
                ?: throw Exception("User not found")
            Result.success(userData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("No user logged in")

            // Delete user profile from Firestore
            usersCollection.document(userId).delete().await()

            // Delete user from Firebase Auth
            auth.currentUser?.delete()?.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun logoutUser() {
        auth.signOut()
    }
}
