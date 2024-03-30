package com.example.spendsavvy.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendsavvy.animation.bounceClick
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