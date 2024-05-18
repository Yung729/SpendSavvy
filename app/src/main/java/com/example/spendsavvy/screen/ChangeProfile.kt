@file:Suppress("NAME_SHADOWING")

package com.example.spendsavvy.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.R
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.repo.FireAuthRepository
import com.example.spendsavvy.viewModels.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

@Composable
fun ChangeProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    fireAuthRepository: FireAuthRepository,
    profileViewModel: ProfileViewModel
) {

    val userData by profileViewModel.userData.observeAsState(UserData())
    var showOption by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val userEmail = userData.email

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            photoUri = it
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        if (bitmap != null) {
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
        } else {
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
                            text = stringResource(id = R.string.chooseImg),
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
                            Text(stringResource(id = R.string.gallery))
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
                        Text(text = stringResource(id = R.string.cancel))
                    }
                },
                modifier = Modifier.width(280.dp)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = stringResource(id = R.string.userName),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 10.dp),
        )
        OutlinedTextFieldItem(
            label = stringResource(id = R.string.userName),
            placeholder = userData.userName,
            value = userData.userName,
            onValueChange = { userData.userName = it },
            height = 70.dp,
        )
        Text(
            text = stringResource(id = R.string.email),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 10.dp),
        )
        OutlinedTextFieldItem(
            label = stringResource(id = R.string.email),
            placeholder = userData.email,
            value = userData.email,
            keyboardType = KeyboardType.Email,
            onValueChange = { userData.email = it },
            height = 70.dp,
        )
        Text(
            text = stringResource(id = R.string.phoneNo),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 10.dp),
        )
        OutlinedTextFieldItem(
            label = stringResource(id = R.string.phoneNo),
            placeholder = userData.phoneNo,
            value = userData.phoneNo,
            keyboardType = KeyboardType.Phone,
            onValueChange = { userData.phoneNo = it },
            height = 70.dp,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            onClick = {
                if (userEmail != userData.email) {
                    fireAuthRepository.changeEmail(
                        userData.email,
                        onSuccess = {
                            saveChangesToFirebase(
                                context,
                                userData,
                                navController,
                                photoUri
                            )
                        })
                }else {
                    saveChangesToFirebase(context, userData, navController, photoUri)
                }
            }, // Pass photoUri to saveChangesToFirebase
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(stringResource(id = R.string.saveChanges))
        }
    }
}

private fun saveChangesToFirebase(
    context: Context,
    userData: UserData,
    navController: NavController,
    photoUri: Uri?
) {
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid ?: ""
    val db = Firebase.firestore
    val usersCollection = db.collection("Users")

    if (photoUri != null) {
        val storageRef = Firebase.storage.reference
        val photoRef = storageRef.child("user_photos/$uid.jpg")

        // Upload the image to Firebase Storage
        val uploadTask = photoRef.putFile(photoUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { url ->
                // Update the photoURL field in the userData object
                val updatedUserData = userData.copy(photoURL = url.toString())
                // Save all user data to Firestore
                usersCollection.document(uid)
                    .set(updatedUserData)
                    .addOnSuccessListener {
                        Log.d(TAG, "User data updated successfully")
                        Toast.makeText(
                            context,
                            "User data updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigateUp()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating user data", e)
                        Toast.makeText(context, "Error updating user data", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error uploading image", e)
            Toast.makeText(context, "Error uploading image", Toast.LENGTH_SHORT).show()
        }
    } else {
        // Save all user data to Firestore without photo
        usersCollection.document(uid)
            .set(userData)
            .addOnSuccessListener {
                Log.d(TAG, "User data updated successfully")
                Toast.makeText(context, "User data updated successfully", Toast.LENGTH_SHORT).show()
                navController.navigateUp()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating user data", e)
                Toast.makeText(context, "Error updating user data", Toast.LENGTH_SHORT).show()
            }
    }
}


@Composable
fun OutlinedTextFieldItem(
    label: String,
    placeholder: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    height: Dp = TextFieldDefaults.MinHeight,
) {
    val (textValue, setTextValue) = remember { mutableStateOf(value) }

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
        label = { Text(text = "", color = Color.Black) },
        placeholder = { Text(text = placeholder, color = Color.Gray) },
        singleLine = true
    )
}
