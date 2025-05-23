package com.example.lockerin.presentation.ui.screens.user

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lockerin.R
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.LoadingScreen
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.delay

import org.koin.androidx.compose.koinViewModel

sealed class AvatarOption {
    data class Color(val color: androidx.compose.ui.graphics.Color) : AvatarOption()
    data class Image(val avatarResId: Int) : AvatarOption()
}

@Composable
fun ChooseAvatarScreen(
    userID: String,
    navController: NavHostController,
    userViewModel: UsersViewModel = koinViewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
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
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf<AvatarOption?>(null) }

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
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Elegir color",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                    ColorChoose(
                        selectedOption = selectedOption,
                        onColorSelected = { color ->
                            selectedOption = AvatarOption.Color(color)
                            val avatarString = avatarOptionToString(selectedOption!!)
                            Log.e("avatar", avatarString)
                            userViewModel.updateAvatar(userID, avatarString, 0)
                        }
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Elegir avatar",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                    DefaultAvatar(
                        selectedOption = selectedOption,
                        onAvatarSelected = { avatar ->
                            selectedOption = AvatarOption.Image(avatar)
                            val resourceName = context.resources.getResourceEntryName(avatar)
                            userViewModel.updateAvatar(userID, resourceName, 1)
                        }
                    )

                }
            }
        )
    }

}

@Composable
fun ColorChoose(
    selectedOption: AvatarOption?,
    onColorSelected: (Color) -> Unit
) {
    val colors = listColor()

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        colors.forEach { color ->
            val isSelected = selectedOption is AvatarOption.Color && selectedOption.color == color

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable {
                        onColorSelected(color)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Log.e("avatar", color.toString())
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
fun DefaultAvatar(
    selectedOption: AvatarOption?,
    onAvatarSelected: (Int) -> Unit
) {
    val avatars = listAvatar()

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        avatars.forEach { avatar ->
            val isSelected =
                selectedOption is AvatarOption.Image && selectedOption.avatarResId == avatar
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .clickable {
                        onAvatarSelected(avatar)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize()
                )

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color(0x80000000), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Log.e("avatar", avatar.toString())
                    }
                }
            }
        }
    }
}

fun listAvatar(): List<Int> {
    return listOf(
        R.drawable.hombre1,
        R.drawable.hombre2,
        R.drawable.hombrenegro,
        R.drawable.hombrepelopincho,
        R.drawable.hombreblancopelocorto,
        R.drawable.mujer1,
        R.drawable.mujer2,
        R.drawable.mujerpelirroja,
        R.drawable.mujerblancacoleta,
        R.drawable.mujerblamcapelocorto
    )

}




fun avatarOptionToString(option: AvatarOption): String {
    return when (option) {
        is AvatarOption.Color -> option.color.toArgb().toString()
        is AvatarOption.Image -> option.avatarResId.toString()
    }
}



