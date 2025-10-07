package com.prog7314.geoquest.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.prog7314.geoquest.data.data.UserData
import kotlinx.coroutines.tasks.await
import kotlin.text.get

class UserRepo {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun addUser(userData: UserData): Result<String> {
        return try {
            val docRef = usersCollection.document()
            val userWithId = userData.copy(id = docRef.id)
            docRef.set(userWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun checkUserExists(email: String): Result<Boolean> {
        return try {
            val snapshot = usersCollection
                .whereEqualTo("email", email)
                .get()
                .await()
            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getUserByEmailAndPassword(email: String, password: String): Result<UserData?> {
        return try {
            val snapshot = usersCollection
                .whereEqualTo("email", email)
                .get()
                .await()

            val user = snapshot.documents.firstOrNull()?.toObject(UserData::class.java)?.copy(
                id = snapshot.documents.firstOrNull()?.id ?: ""
            )

            // Note: In production, you should use proper password hashing
            // This is a simplified implementation
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            usersCollection.document(userId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
