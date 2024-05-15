package com.example.spendsavvy.screen

import android.content.ContentValues
import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.R
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MyProfileScreen(modifier: Modifier = Modifier, navController: NavController,profileViewModel: ProfileViewModel) {

    val userData by profileViewModel.userData.observeAsState(UserData())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalArrangement = Arrangement.Top,
    ) {
            Row() {
                Spacer(modifier = Modifier.weight(1.5f))

                Image(
                    painter = if (userData.photoURL!!.isNotEmpty()) {
                        rememberAsyncImagePainter(model = userData.photoURL)
                    } else {
                        painterResource(id = R.drawable.profile_icon)
                    },
                    contentDescription = "User profile image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = CircleShape
                        )
                )

                Spacer(modifier = Modifier.weight(1f)) // Spacer to push position

                Image(
                    painter = painterResource(R.drawable.edit_profile_icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { navController.navigate(Screen.ChangeProfile.route) }
                )
            }

            Card(
                modifier = Modifier.padding(vertical = 15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                UserInfoRow(label = stringResource(id = R.string.userName), data = userData.userName)
                UserInfoRow(label = stringResource(id = R.string.email), data = userData.email)
                UserInfoRow(label = stringResource(id = R.string.phoneNo), data = userData.phoneNo)
            }
    }
}
@Composable
fun UserInfoRow(label: String, data: String) {
    Column{
        Text(
            text = label,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Text(
            text = data,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 10.dp),
        )
        LineDivider()
    }
}
fun getUserData(userId: String, onDataReceived: (UserData) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection("Users").document(userId)

    docRef.get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val userData = document.toObject(UserData::class.java)
                if (userData != null) {
                    onDataReceived(userData)
                }
            }
        }
        .addOnFailureListener { e ->
            // Handle failure
            Log.w(ContentValues.TAG, "Error getting user data", e)
        }
}

@Preview(showBackground = true)
@Composable
fun MyProfileScreenPreview() {
    MyProfileScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController(),
        profileViewModel = viewModel()
    )
}