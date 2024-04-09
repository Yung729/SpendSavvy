package com.example.spendsavvy.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spendsavvy.animation.bounceClick


@Composable
fun AnalysisScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(state = scrollState)
    ) {
        Button(
            onClick = { },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .bounceClick()
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
            )
        ) {
            Text(
                text = "Set Budget", textAlign = TextAlign.Center, color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        PieChart(
            data = mapOf(
                Pair("Income-1", 150),
                Pair("Sample-2", 120),
                Pair("Sample-3", 110),
                Pair("Sample-4", 170),
                Pair("Sample-5", 120),
            )
        )


    }
}


@Preview(showBackground = true)
@Composable
fun AnalysisScreenPreview() {
    AnalysisScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    )
}