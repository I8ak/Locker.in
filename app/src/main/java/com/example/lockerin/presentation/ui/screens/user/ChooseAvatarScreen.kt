package com.example.lockerin.presentation.ui.screens.user

import android.R.attr.key
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.Icon

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.LoadingScreen
import com.example.lockerin.presentation.ui.components.randomColor
import com.example.lockerin.presentation.viewmodel.rentals.RentalViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChooseAvatarScreen(
    userID: String,
    navController: NavHostController,
    userViewModel: UsersViewModel= koinViewModel(),
    authViewModel: AuthViewModel=viewModel()
){
    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }
    if (userID == null) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
            }
        }
        return
    }
    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    val user = userViewModel.getUserById(userID)
    Log.d("usuario", userID)
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
    }
    if (isLoading) {
        LoadingScreen(isLoading = isLoading)
    } else {
        DrawerMenu(
            textoBar = "Elegir avatar",
            navController = navController,
            authViewModel = viewModel(),
            fullUser = userState,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it)
                        .background(Color.Transparent)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Elegir color",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                    ColorChoose(userID)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Elegir avatar",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )


                    Spacer(modifier = Modifier.padding(8.dp))
                    CamaraGalery()

                }
            }
        )
    }

}

@Composable
fun ColorChoose(userID: String) {
    val colors = listColor()
    var selectedColor by remember { mutableStateOf<Color?>(null) }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable {
                        selectedColor = color
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedColor == color) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

fun listColor(): List<Color> {
    return listOf(
        Color(0xFFE57373), // Rojo claro
        Color(0xFF64B5F6), // Azul claro
        Color(0xFF81C784), // Verde claro
        Color(0xFF673AB7), // Morado
        Color(0xFFBA68C8), // Lila
        Color(0xFFFF8A65), // Naranja claro
        Color(0xFFA1887F), // Marrón claro
        Color(0xFF4DD0E1), // Turquesa
        Color(0xFF90A4AE), // Gris azulado
        Color(0xFF4CAF50)  // Verde
    )
}

@Composable
fun CamaraGalery(){
    var isSelected by remember { mutableStateOf(false) }
    val arrowIcon = if (isSelected) {
        Icons.Default.KeyboardDoubleArrowUp
    } else {
        Icons.Default.KeyboardDoubleArrowDown
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                isSelected = !isSelected
            }
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Elegir imagen",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                imageVector = arrowIcon,
                contentDescription = "Ayuda",
                tint = Color.Black,
            )
        }

        if (isSelected) {
            Row (
                Modifier.fillMaxWidth()
                    .padding(8.dp)
            ){
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Camara",
                    tint = Color.Black,
                    modifier = Modifier.clickable {

                    }
                        .weight(1f)
                        .size(80.dp)
                )

                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Galeria",
                    tint = Color.Black,
                    modifier = Modifier.clickable {

                    }
                        .weight(1f)
                        .size(80.dp)
                )
            }

        }
    }
}