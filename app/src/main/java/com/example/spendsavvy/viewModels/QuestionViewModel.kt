package com.example.spendsavvy.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Question
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch
import java.util.UUID

class QuestionViewModel(
    context: Context,
    isOnline: Boolean,
    userId: String,
) : ViewModel() {

    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)
    private val internet = isOnline
    private val currentContext = context
    private val currentUserId = userId
    val isLoading = MutableLiveData<Boolean>()

    val allQuestionsList = MutableLiveData<List<Question>>() // All questions List
    val pendingQuestionsList = MutableLiveData<List<Question>>() // pending questions List
    val answeredQuestionsList = MutableLiveData<List<Question>>() // answered questions List

    init {
        getQuestionsRecord()
    }

    fun getQuestionsRecord() {
        viewModelScope.launch {
            val questionsFromDB: List<Question>

            isLoading.value = true
            try {
                questionsFromDB = if (internet) {
                    firestoreRepository.readItemsFromDatabase(
                        currentUserId,
                        "Questions",
                        Question::class.java
                    )
                } else {
                    dbHelper.readQuestions(userId = currentUserId)
                }

                updateQuestions(questionsFromDB)

            } catch (e: Exception) {
                Log.e(TAG, "Error getting questions", e)
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    private suspend fun getQuestionId(question: Question): String {
        return firestoreRepository.getDocumentId("Questions", currentUserId, question)
    }

    private fun updateQuestions(questions: List<Question>) {
        viewModelScope.launch {
            val pendingQuestions = questions.filter { it.status == "PENDING" }
            val answeredQuestions = questions.filter { it.status == "ANSWERED" }

            allQuestionsList.postValue(questions)
            pendingQuestionsList.postValue(pendingQuestions)
            answeredQuestionsList.postValue(answeredQuestions)
        }
    }

    fun addQuestionToFirestore(
        question: Question,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                firestoreRepository.addItem(
                    currentUserId,
                    "Questions",
                    question,
                    "Q%04d",
                    onSuccess = { documentId ->
                        dbHelper.addNewQuestion(
                            questionId = documentId,
                            questionText = question.questionText,
                            answer = question.answer,
                            status = question.status,
                            questionDate = question.questionDate,
                            userId = currentUserId
                        )
                        val currentQuestions = allQuestionsList.value ?: emptyList()
                        val updatedQuestions = currentQuestions + question
                        updateQuestions(updatedQuestions)
                        onSuccess()
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "Error adding question", exception)
                        onFailure(exception)
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error adding question", e)
                onFailure(e)
            }
        }
    }

//    fun editQuestion(question: Question, updatedQuestion: Question) {
//        viewModelScope.launch {
//            try {
//                val questionId: String = getQuestionId(question)
//
//                firestoreRepository.updateItemInFirestoreById(
//                    currentUserId,
//                    "Questions",
//                    questionId,
//                    updatedQuestion,
//                    onSuccess = {
//                        dbHelper.updateQuestion(
//                            questionId = questionId,
//                            questionText = updatedQuestion.questionText,
//                            answer = updatedQuestion.answer,
//                            status = updatedQuestion.status,
//                            questionDate = updatedQuestion.questionDate,
//                            userId = currentUserId
//                        )
//                        val currentQuestions = allQuestionsList.value ?: emptyList()
//                        val updatedQuestionsList = currentQuestions.map {
//                            if (it == question) updatedQuestion else it
//                        }
//                        updateQuestions(updatedQuestionsList)
//                        Toast.makeText(
//                            currentContext, "Question edited", Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                )
//
//            } catch (e: Exception) {
//                Log.e(TAG, "Error editing Question", e)
//            }
//        }
//    }


    fun deleteQuestion(question: Question) {
        viewModelScope.launch {
            try {
                val questionId: String =
                    firestoreRepository.getDocumentId("Questions", currentUserId, question)

                firestoreRepository.deleteItemFromFirestoreById(
                    currentUserId,
                    "Questions",
                    questionId,
                    onSuccess = {
                        dbHelper.deleteQuestion(questionId, currentUserId)
                        val currentQuestions = allQuestionsList.value ?: emptyList()
                        val updatedQuestions = currentQuestions.filter { it != question }
                        updateQuestions(updatedQuestions)
                    }
                )

            } catch (e: Exception) {
                Log.e(TAG, "Error deleting question", e)
            }
        }
    }

    fun generateQuestionId(): String {
        val random = UUID.randomUUID().toString().substring(0, 5)
        return "Q$random"
    }
}
