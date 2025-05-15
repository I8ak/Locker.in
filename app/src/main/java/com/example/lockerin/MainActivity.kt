package com.example.lockerin

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.presentation.navigation.NavGraph

import com.example.lockerin.presentation.ui.theme.LockerinTheme




class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LockerinTheme {
                NavGraph(
                    navController = rememberNavController(),
                    authViewModel = viewModel()
                )
            }
        }
    }
}


