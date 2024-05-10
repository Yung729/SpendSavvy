@file:Suppress("NAME_SHADOWING")

package com.example.spendsavvy.screen

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.R
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.IOException

@Composable
fun ChangeProfileScreen(modifier: Modifier = Modifier, navController: NavController) {

    var userData by remember { mutableStateOf(UserData("","","", "", "", "")) }
    val auth = FirebaseAuth.getInstance()
    getUserData(auth.currentUser?.uid ?: "") { user ->
        userData = user
    }
    var showOption by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
            // Upload image to Firebase Storage
            uploadImageToFirebaseStorage(context, it, userData)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        if(bitmap != null) {
            Image(
                bitmap = bitmap?.asImageBitmap()!!,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(150.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = CircleShape
                    )
                    .clickable { showOption = true },
            )
        }
        else {
            Image(
                painter = if (userData.photoURL!!.isNotEmpty()) {
                    rememberAsyncImagePainter(model = userData.photoURL)
                } else {
                    painterResource(id = R.drawable.profile_icon)
                },
                contentDescription = "User profile image",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = CircleShape
                    )
                    .clickable { showOption = true },
                )
        }
        if (showOption) {
            AlertDialog(
                onDismissRequest = { showOption = false },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Choose Image Source",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                        Image(
                            painter = painterResource(R.drawable.cross_icon),
                            contentDescription = "Close",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { showOption = false }
                                .padding(8.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            launcher.launch("image/*")
                            showOption = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.gallery_icon),
                                contentDescription = "Gallery Icon",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Gallery")
                        }
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showOption = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(text = "Cancel")
                    }
                },
                modifier = Modifier.width(280.dp)
            )
        }

        Spacer(modifier = Modifier.height(15.dp)) // Spacer added here

        OutlinedTextFieldItem(
            label = "User Name",
            placeholder = userData.userName,
            value = userData.userName,
            onValueChange = { userData.userName = it },
            height = 60.dp
        )

        OutlinedTextFieldItem(
            label = "Email Address",
            placeholder = userData.email,
            value = userData.email,
            keyboardType = KeyboardType.Email,
            onValueChange = { userData.email = it},
            height = 60.dp
        )

        OutlinedTextFieldItem(
            label = "Phone Number",
            placeholder = userData.phoneNo,
            value = userData.phoneNo,
            keyboardType = KeyboardType.Phone,
            onValueChange = { userData.phoneNo = it},
            height = 60.dp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            onClick = { saveChangesToFirebase(context, userData) }, // Pass photoUrl to saveChangesToFirebase
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(text = "Save Changes")
        }
    }

}
private fun uploadImageToFirebaseStorage(context: Context, uri: Uri, userData: UserData) {
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid ?: ""
    val storageRef = Firebase.storage.reference

    // Create a reference to "user_photos/$uid.jpg"
    val photoRef = storageRef.child("user_photos/$uid.jpg")

    // Upload the image to Firebase Storage
    val uploadTask = photoRef.putFile(uri)
    uploadTask.addOnSuccessListener { taskSnapshot ->
        // Get the download URL from the task snapshot
        taskSnapshot.storage.downloadUrl.addOnSuccessListener { url ->
            // Update the photoURL field in the userData object
            val updatedUserData = userData.copy(photoURL = url.toString())
            saveChangesToFirebase(context, updatedUserData)
        }
    }.addOnFailureListener { e ->
        // Handle failure
        Log.w(TAG, "Error uploading image", e)
        Toast.makeText(context, "Error uploading image", Toast.LENGTH_SHORT).show()
    }
}


private fun saveChangesToFirebase(context: Context, userData: UserData) {
    // Update user data in Firestore
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid ?: ""
    val db = Firebase.firestore

    val usersCollection = db.collection("Users")
    usersCollection.document(uid)
        .set(userData)
        .addOnSuccessListener {
            // Handle success
            Log.d(TAG, "User data updated successfully")
            Toast.makeText(context, "User data updated successfully", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            // Handle failure
            Log.w(TAG, "Error updating user data", e)
            Toast.makeText(context, "Error updating user data", Toast.LENGTH_SHORT).show()
        }
}

@Composable
fun OutlinedTextFieldItem(
    label: String,
    placeholder: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    height: Dp = TextFieldDefaults.MinHeight
) {
    val (textValue, setTextValue) = remember { mutableStateOf(value) }

    // Update textValue with the value of 'value' when it changes
    LaunchedEffect(value) {
        setTextValue(value)
    }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            setTextValue(it)
            onValueChange(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .background(Color.White)
            .height(height),
        textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        label = { Text(text = label, color = Color.Black) },
        placeholder = { Text(text = placeholder, color = Color.Gray) },
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun ChangeProfileScreenPreview() {
    ChangeProfileScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}