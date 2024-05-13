package com.example.spendsavvy.repo

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.viewModels.CategoryViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore

class FireAuthRepository(
    val context: Context,
    val navController: NavController,
    private val categoryViewModel: CategoryViewModel
) {

    private var auth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val userId = getCurrentUserId()
                    navController.navigate(route = Screen.MainScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }

            } else if (!task.isSuccessful) {
                Toast.makeText(
                    context, "Unsuccessful to Sign In Account", Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    fun signUp(email: String, password: String, userName: String, phoneNo: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    if (user != null) {
                        val uid = user.uid
                        val userData = UserData(uid, "", userName, phoneNo, email, password)
                        val db = Firebase.firestore
                        val usersCollection = db.collection("Users")
                        usersCollection.document(uid)
                            .set(userData)


                        categoryViewModel.initializeCategoryToFirestore(user.uid)
                    }

                    navController.navigate(route = Screen.Login.route)

                } else {
                    Toast.makeText(
                        context,
                        "Unsuccessful to Sign Up Account",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    fun signOut() {
        auth.signOut()
        navController.navigate("First") {
            popUpTo("Second") {
                inclusive = true
            }
        }
    }

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Password reset email sent successfully",
                        Toast.LENGTH_SHORT
                    ).show()


                } else {
                    Toast.makeText(
                        context,
                        "Failed to send password reset email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun updateUser(
        userId: String,
        newPassword: String
    ) {
        val db = Firebase.firestore
        val docRef = db.collection("Users").document(userId)

        docRef.update("password", newPassword)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Password successfully updated in Firestore")

            }
            .addOnFailureListener { e ->
                Log.d(ContentValues.TAG, "Error updating password in Firestore: $e")

            }
    }

    fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: ""
    }

    fun getCurrentUser(): FirebaseUser {
        return auth.currentUser as FirebaseUser
    }

}