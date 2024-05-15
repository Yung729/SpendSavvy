package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.example.spendsavvy.components.SwipeToDeleteItem
import com.example.spendsavvy.models.Question
import com.example.spendsavvy.viewModels.QuestionViewModel
import java.util.Date

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HelpAndSupport(modifier: Modifier = Modifier, navController: NavController, questionsViewModel: QuestionViewModel) {

    val context = LocalContext.current
    val options = mutableStateListOf(stringResource(id = R.string.pending), stringResource(id = R.string.answered))
    var selectedIndex by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var questionText by remember { mutableStateOf("") }
    val pendingQuestionsList by questionsViewModel.pendingQuestionsList.observeAsState(initial = emptyList())
    val answeredQuestionsList by questionsViewModel.answeredQuestionsList.observeAsState(initial = emptyList())
    val dismissStateMap = remember { mutableMapOf<Question, DismissState>() }

    Scaffold(
        floatingActionButton = {
            // FloatingActionButton
            Column {
                FloatingActionButton(
                    onClick = {
                        sendCallIntent(context)
                    },
                    modifier = Modifier
                        .size(95.dp, 50.dp)
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
                Spacer(modifier = Modifier.height(10.dp))
                FloatingActionButton(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier
                        .size(95.dp, 50.dp)
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
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = stringResource(id = R.string.text_18),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
            ) {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                ) {
                    options.forEachIndexed { index, option ->
                        SegmentedButton(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index, count = options.size
                            )
                        ) {
                            Text(
                                text = option,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }

            when (selectedIndex) {
                0 -> QuestionList(pendingQuestionsList, questionsViewModel,dismissStateMap)
                1 -> QuestionList(answeredQuestionsList, questionsViewModel,dismissStateMap)
            }

        }
    }
    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.text_20),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                OutlinedTextField(
                    value = questionText,
                    onValueChange = { questionText = it },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text(stringResource(id = R.string.text_21)) }
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
                        Text(text = stringResource(id = R.string.cancel))
                    }

                    Button(
                        onClick = {
                            if(questionText.isNotEmpty()){
                                questionsViewModel.addQuestionToFirestore(
                                    Question(
                                        id = questionsViewModel.generateQuestionId(),
                                        questionText = questionText,
                                        answer = "WAITING FOR REPLY....",
                                        status = "PENDING",
                                        questionDate = Date(),
                                    ),
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Questions sent successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        questionText = ""
                                    }
                                ) {
                                    Toast.makeText(
                                        context,
                                        "Fail to send questions",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                showDialog = false
                            }
                            else
                            {
                                Toast.makeText(
                                    context,
                                    "Cannot send empty question",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = stringResource(id = R.string.submit))
                    }
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuestionList(
    questions: List<Question>,
    questionViewModel: QuestionViewModel,
    dismissStateMap: MutableMap<Question, DismissState>
) {
    var showEditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(questions) { question ->
            QuestionItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                    },
                question = question,
                questionViewModel = questionViewModel,
                dismissState = dismissStateMap[question]
                    ?: rememberDismissState().also { dismissStateMap[question] = it }
            )
        }
    }
}
@Composable
fun EditQuestionDialog(
    question: Question,
    questionViewModel: QuestionViewModel,
    onDismiss: () -> Unit
){
    var updatedQuestionText by remember { mutableStateOf(question.questionText) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit your question",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            OutlinedTextField(
                value = updatedQuestionText,
                onValueChange = { updatedQuestionText = it },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(120.dp)
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = onDismiss ,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.DarkGray,
                    )
                ) {
                    Text(text =stringResource(id = R.string.cancel))
                }
                Button(
                    onClick = {

//                        questionViewModel.editQuestion(
//                            question = question,
//                            updatedQuestion = Question(
//                                id = question.id,
//                                questionText = updatedQuestionText,
//                                answer = "WAITING FOR REPLY....",
//                                status = "PENDING",
//                                questionDate = Date(),
//                            ),
//                        )
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(id = R.string.saveChanges))
                }
            }
        }
    )
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuestionItem(
    modifier: Modifier = Modifier,
    question: Question,
    questionViewModel: QuestionViewModel,
    dismissState: DismissState
) {
    val context = LocalContext.current

    SwipeToDeleteItem(state = dismissState) {

        if (!dismissState.isDismissed(DismissDirection.StartToEnd)) {
            Card(
                modifier = modifier
                    .padding(vertical = 5.dp, horizontal = 10.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Dropdown(text = question.questionText)
                    {
                        Text(
                            text = question.answer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .background(Color.LightGray)
                        )
                    }
                }
            }
        }
        if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
            questionViewModel.deleteQuestion(question)
            Toast.makeText(
                context,
                "Question has been deleted successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun Dropdown(text: String, initiallyOpened: Boolean = false, content: @Composable () -> Unit ){

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

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp))
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
                    .scale(1.85f, if (isOpen) -1f else 1f)
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
        navController = rememberNavController(),
        questionsViewModel = viewModel()
    )
}