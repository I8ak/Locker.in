package com.example.lockerin.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.R
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    modifier: Modifier= Modifier
){
    Box(
        modifier= modifier
            .fillMaxWidth()
            .background(BeigeClaro),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(R.drawable.lockerlogo),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxWidth(),
        )
    }
    LaunchedEffect(Unit) {
        delay(1000)
        navController.navigate(Screen.Login.route){
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}