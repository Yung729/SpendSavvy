package com.example.spendsavvy.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.text.AnnotatedString
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
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.ButtonColor
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@Composable
fun ChangeProfileScreen(modifier: Modifier = Modifier, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = "Up",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(40.dp)
            )
            Text(
                text = "My Profile",
                fontFamily = poppinsFontFamily,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeaderTitle,
                modifier = Modifier
                    .padding(end = 130.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Image(
                painter = painterResource(R.drawable.add_image_icon),
                contentDescription = "User profile image",
                modifier = Modifier
                    .size(125.dp)
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(Color.Gray),
            )

            Card(
                modifier = Modifier.padding(vertical = 15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "User Name",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
//                EditableText(
//                    text = userName,
//                    onTextChanged = { userName = it },
//                    placeholder = "Enter your name"
//                )
                LineDivider()
                Text(
                    text = "Email",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                LineDivider()
                Text(
                    text = "Phone Number",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                LineDivider()
                Text(
                    text = "Address",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                LineDivider()
            }
        }
    }
}
//@Composable
//fun EditableUserDataScreen() {
//    var userName by remember { mutableStateOf("John Doe") }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        EditableText(
//            text = userName,
//            onTextChanged = { userName = it },
//            placeholder = "Enter your name"
//        )
//    }
//}
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