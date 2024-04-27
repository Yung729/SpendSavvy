package com.example.spendsavvy.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDeleteItem(
    state: DismissState,
    content: @Composable RowScope.() -> Unit
) {
    SwipeToDismiss(
        state = state,
        directions = setOf(DismissDirection.StartToEnd),
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
        },
        background = {
            val direction = state.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (state.targetValue) {
                    DismissValue.Default -> Color.LightGray.copy(alpha = 0.5f)
                    else -> Color.Red
                }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val scale by animateFloatAsState(
                if (state.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove item",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = content
    )
}