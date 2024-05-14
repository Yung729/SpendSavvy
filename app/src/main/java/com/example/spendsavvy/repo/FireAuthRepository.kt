package com.example.spendsavvy.repo

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.viewModels.CategoryViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore

class FireAuthRepository(
    val context: Context,
    private val categoryViewModel: CategoryViewModel,
    internet: Boolean
) {

    private var auth = FirebaseAuth.getInstance()
    val connection = internet

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("UserAcc", Context.MODE_PRIVATE)
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {

        if (connection) {

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val currentUser = auth.currentUser

                    if (currentUser != null && currentUser.isEmailVerified) {
                        saveCredentials(email, password, currentUser.uid)
                        onSuccess()
                    } else if (currentUser != null && !currentUser.isEmailVerified) {
                        Toast.makeText(
                            context,
                            "Please verify your email address before signing in.",
                            Toast.LENGTH_SHORT
                        ).show()
                        auth.signOut()
                    }

                } else if (!task.isSuccessful) {
                    Toast.makeText(
                        context, "Unsuccessful to Sign In Account", Toast.LENGTH_SHORT
                    ).show()

                }
            }

        } else {

            val offlineEmail = sharedPreferences.getString("email", null)
            val offlinePassword = sharedPreferences.getString("password", null)


            if (offlineEmail != null && offlinePassword != null) {
                if (offlineEmail == email && password == offlinePassword) {
                    onSuccess()

                } else {
                    Toast.makeText(
                        context, "Unsuccessful to Sign In Account", Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }


    }

    private fun saveCredentials(email: String, password: String, userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putString("userID", userId)
        editor.apply()
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

                    user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            // Email sent successfully
                            Toast.makeText(
                                context,
                                "Verification email sent. Please check your inbox.",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            // Failed to send verification email
                            Toast.makeText(
                                context,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

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