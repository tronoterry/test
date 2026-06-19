package com.nrltipping.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nrltipping.app.data.model.UserProfile
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {
    val currentUserId: String?
        get() = auth.currentUser?.uid

    fun isSignedIn(): Boolean = auth.currentUser != null

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, password: String, displayName: String) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: error("Sign up failed: no user id returned")
        val profile = UserProfile(uid = uid, displayName = displayName, email = email, isAdmin = false)
        firestore.collection("users").document(uid).set(profile).await()
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun fetchProfile(uid: String): UserProfile? {
        val snapshot = firestore.collection("users").document(uid).get().await()
        return snapshot.toObject(UserProfile::class.java)
    }
}
