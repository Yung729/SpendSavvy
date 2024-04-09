package com.example.spendsavvy.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily

@Composable
fun ButtonComponent(onButtonClick: () -> Unit, text: String) {
    Button(
        onClick = onButtonClick,
        modifier = Modifier
            .padding(bottom = 10.dp, top = 10.dp)
            .bounceClick(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        )
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

@Composable
fun HeaderTitle(text: String) {
    Text(
        text = text,
        fontFamily = poppinsFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = HeaderTitle,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderTopBar(text: String, canNavBack: Boolean, navUp: () -> Unit) {
    TopAppBar(
        title = {
            HeaderTitle(text = text)
        },
        navigationIcon = {
            if (canNavBack) {
                IconButton(onClick = navUp) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back Key"
                    )
                }
            }
        }
    )
}