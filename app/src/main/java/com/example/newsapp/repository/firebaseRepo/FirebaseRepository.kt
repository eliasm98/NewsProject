package com.example.newsapp.repository.firebaseRepo

import com.example.newsapp.data.datastore.UserDatastore
import com.example.newsapp.data.firebase.FirestoreUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDatastore: UserDatastore
) {

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("UID not found"))

            userDatastore.saveUid(uid)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(): FirestoreUser? {
        return try {
            val uid = userDatastore.getUid() ?: return null
            val document = firestore.collection("users").document(uid).get().await()
            document.toObject(FirestoreUser::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Unit> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("UID not found"))
            val user = FirestoreUser(
                userId = uid,
                email = email,
                firstName = firstName,
                lastName = lastName
            )
            firestore.collection("users").document(uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: FirestoreUser): Result<Unit> {
        return try {
            val uid = userDatastore.getUid() ?: return Result.failure(Exception("UID not found"))
            firestore.collection("users").document(uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}