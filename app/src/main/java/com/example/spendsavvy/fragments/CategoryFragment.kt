package com.example.spendsavvy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.spendsavvy.screen.CategoryScreen
import com.example.spendsavvy.ui.theme.SpendSavvyTheme
import com.example.spendsavvy.viewModels.CategoryViewModel
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels

class CategoryFragment : Fragment() {

    private val catViewModel: CategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return ComposeView(requireContext()).apply {
            setContent {
                SpendSavvyTheme {
                    // Pass the view model to the CategoryScreen
                    CategoryScreen(modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        , catViewModel = catViewModel)
                }
            }
        }
    }
}