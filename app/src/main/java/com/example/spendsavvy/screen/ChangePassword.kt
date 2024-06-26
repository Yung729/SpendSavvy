package com.example.spendsavvy.screen

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.example.spendsavvy.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun ChangePassword(modifier: Modifier = Modifier, navController: NavController) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }

    var userData by remember { mutableStateOf(UserData("", "", "", "", "", "")) }
    val auth = FirebaseAuth.getInstance()
    getUserData(auth.currentUser?.uid ?: "") { user ->
        userData = user
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(id = R.string.changePw),
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(id = com.example.spendsavvy.R.string.text_13),
            color = Color.Gray,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        )

        PasswordTextField(
            labelText = stringResource(id = com.example.spendsavvy.R.string.currentPw),
            value = oldPassword,
            onValueChange = { oldPassword = it },
            passwordVisible = passwordVisible,
            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
            userData = userData
        )

        PasswordTextField(
            labelText = stringResource(id = com.example.spendsavvy.R.string.new_text),
            value = newPassword,
            onValueChange = { newPassword = it },
            passwordVisible = passwordVisible,
            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
            userData = userData
        )

        SavePasswordButton(
            oldPassword = oldPassword,
            newPassword = newPassword,
            userData = userData,
            showDialog = showDialog,
            isPasswordValid = isPasswordValid,
            onShowDialogChange = { showDialog = it },
            onIsPasswordValidChange = { isPasswordValid = it }
        )
    }
}

@Composable
fun SavePasswordButton(
    oldPassword: String,
    newPassword: String,
    userData: UserData,
    showDialog: Boolean,
    isPasswordValid: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    onIsPasswordValidChange: (Boolean) -> Unit
) {
    Button(
        onClick = {
            if (oldPassword.isNotEmpty() && newPassword.isNotEmpty() && passwordValidation(oldPassword) && passwordValidation(newPassword)) {
                if (userData.password == oldPassword) {
                    saveNewPasswordToFirestore(newPassword)
                    onShowDialogChange(true)
                    onIsPasswordValidChange(true)
                } else {
                    onShowDialogChange(true)
                    onIsPasswordValidChange(false)
                }
            } else {
                onShowDialogChange(true)
                onIsPasswordValidChange(false)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray,
            contentColor = Color.White
        )
    ) {
        Text(
            text = stringResource(id = com.example.spendsavvy.R.string.saveNewPw),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }

    if (showDialog) {
        val title = if (isPasswordValid) stringResource(id = com.example.spendsavvy.R.string.success) else stringResource(id = com.example.spendsavvy.R.string.fail)
        val message = if (isPasswordValid) stringResource(id = com.example.spendsavvy.R.string.successPw) else stringResource(id = com.example.spendsavvy.R.string.failPw)

        AlertDialog(
            onDismissRequest = { onShowDialogChange(false) },
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    color = if (isPasswordValid) Color.Green else Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onShowDialogChange(false) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = stringResource(id = com.example.spendsavvy.R.string.ctn))
                    }
                }
            }
        )
    }
}
fun saveNewPasswordToFirestore(newPassword: String) {
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid ?: ""
    val db = Firebase.firestore

    val usersCollection = db.collection("Users")
    usersCollection.document(uid)
        .update("password", newPassword)
        .addOnSuccessListener {
            Log.d(TAG, "Password updated successfully")
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error updating password", e)
        }
}
@Composable
fun PasswordTextField(
    labelText: String,
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    userData: UserData
) {
    val passIcon = if (passwordVisible) painterResource(id = R.drawable.show_pass)
    else painterResource(id = R.drawable.hide_pass)

    Column {
        Text(
            text = labelText,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = Color.Gray, fontSize = 20.sp),
            label = { Text(text = labelText) },
            trailingIcon = {
                IconButton(onClick = onPasswordVisibilityToggle) {
                    Icon(
                        painter = passIcon,
                        contentDescription = "PassIcon",
                        modifier = Modifier.size(35.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, top = 10.dp)
                .background(Color.White),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
        )
    }
}
@Preview(showBackground = true)
@Composable
fun ChangePasswordPreview() {
    ChangePassword(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}