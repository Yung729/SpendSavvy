package com.example.spendsavvy.screen

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.example.spendsavvy.navigation.Screen

@Composable
fun HelpAndSupport(modifier: Modifier = Modifier, navController: NavController) {

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Frequently Asked Questions",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
        )
        Dropdown(
            text = stringResource(id = R.string.q1),
            modifier = Modifier.padding(15.dp)
        ){
            Text(
                text = stringResource(id = R.string.a1),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.LightGray)
            )
        }
        Dropdown(
            text = stringResource(id = R.string.q2),
            modifier = Modifier.padding(15.dp)
        ){
            Text(
                text = stringResource(id = R.string.a2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.LightGray)
            )
        }
        Dropdown(
            text = stringResource(id = R.string.q3),
            modifier = Modifier.padding(15.dp)
        ){
            Text(
                text = stringResource(id = R.string.a3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.LightGray)
            )
        }

        FloatingActionButton(
            onClick = {
                sendCallIntent(context)
            },
            modifier = Modifier
                .size(95.dp, 50.dp)
                .offset(
                    x = (125).dp, y = (140).dp

                )
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                ),
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            containerColor = Color.Gray,
            contentColor = Color.White,
        ) {
            Image(
                painter = painterResource(id = R.drawable.support_icon),
                contentDescription = "Support",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }
        FloatingActionButton(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .size(95.dp, 50.dp)
                .offset(
                    x = (125).dp, y = (155).dp
                )
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                ),
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            containerColor = Color.Gray,
            contentColor = Color.White,
        ) {
            Image(
                painter = painterResource(id = R.drawable.comment_icon),
                contentDescription = "Upload question",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }
        FloatingActionButton(
            onClick = {
                //show history questions
            },
            modifier = Modifier
                .size(95.dp, 50.dp)
                .offset(
                    x = (125).dp, y = (170).dp
                )
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                ),
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            containerColor = Color.Gray,
            contentColor = Color.White,
        ) {
            Image(
                painter = painterResource(id = R.drawable.history_icon),
                contentDescription = "History",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Ask us about your questions",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("Enter your question here.....") }
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            showDialog = false
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.DarkGray,
                        )
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = {
                            //update questions to user firestore
                            try {
                                if (success) {
                                    Toast.makeText(context, "Questions sent successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Fail to send questions", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: NumberFormatException) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            }
                            showDialog = false
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        )
    }
}

@Composable
fun Dropdown(text: String, modifier: Modifier = Modifier, initiallyOpened: Boolean = false, content: @Composable () -> Unit ){

    var isOpen by remember { mutableStateOf(initiallyOpened) }
    val alpha = animateFloatAsState(
        targetValue = if(isOpen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300
        )
    )
    val rotateX = animateFloatAsState(
        targetValue = if(isOpen) 1f else -90f,
        animationSpec = tween(
            durationMillis = 300
        )
    )

    Column(modifier = Modifier.fillMaxWidth())
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ){
            Text(
                text = text,
                color = Color.Black,
                fontSize = 20.sp
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier
                    .clickable {
                        isOpen = !isOpen
                    }
                    .scale(1f, if (isOpen) -1f else 1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0.5f, 0f)
                    rotationX = rotateX.value
                }
                .alpha(alpha.value)
        ){
            content()
        }
    }
}

fun sendCallIntent(context: Context) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:123456789")

    context.startActivity(intent)
}
@Preview(showBackground = true)
@Composable
fun HelpAndSupportPreview() {
    HelpAndSupport(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}