package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import kotlinx.coroutines.launch


data class LanguageSelection(
    val id: Int,
    var isSelected: Boolean,
    val text: String,
    val languageCode: String
)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Language(modifier: Modifier = Modifier, navController: NavController) {

    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val selections = remember {
        mutableStateListOf(
            LanguageSelection(
                id = 0,
                isSelected = true,
                text = "English",
                languageCode = "en"
            ),
            LanguageSelection(
                id = 1,
                isSelected = false,
                text = "Chinese中文",
                languageCode = "zh"
            ),
            LanguageSelection(
                id = 2,
                isSelected = false,
                text = "Bahasa Melayu",
                languageCode = "ms"
            )
        )
    }

    androidx.compose.material.Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        content = {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(id = R.string.login_text),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))

                selections.forEachIndexed { index, info ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(message = "Change language to ${info.text}(${info.languageCode})")
                                    // Deselect all languages
                                    selections.forEach { it.isSelected = false }
                                    // Select the clicked language
                                    info.isSelected = true
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        context.getSystemService(LocaleManager::class.java)
                                            .applicationLocales =
                                            LocaleList.forLanguageTags(info.languageCode)
                                    }
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = info.text,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
//                        if (info.isSelected) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.tick_icon),
//                                contentDescription = "",
//                                modifier = Modifier.size(24.dp)
//                            )
//                        }
                    }
                    LineDivider()
                }
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun LanguagePreview() {
    Language(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}