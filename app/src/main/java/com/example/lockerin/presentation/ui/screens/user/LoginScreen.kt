package com.example.lockerin.presentation.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White


import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.AppViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel


@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel=viewModel(),
    appViewModel: AppViewModel
){
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier.statusBarsPadding(),color = BeigeClaro
    ) {
        var email by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,

        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(text = "Login", fontSize = 40.sp, fontWeight = FontWeight.Bold,color = Color.Black)

                Spacer(modifier = Modifier.padding(20.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(
                            text = "Email",
                            color = Color.Black
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon",tint = Color.Black)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(12.dp)), // Fondo blanco con esquinas redondas
                    shape = RoundedCornerShape(12.dp), // Bordes circulares
                    colors =  OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                var password by remember { mutableStateOf("") }
                var passwordVisible by remember { mutableStateOf(false) }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            text = "Password",
                            color = Color.Black
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password Icon",
                            tint = Color.Black
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color.Black
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "¿Has olvidado la contraseña?",
                    textDecoration = TextDecoration.Underline,
                    color = Color.Black,
                    modifier = Modifier.clickable{
                        navController.navigate(Screen.EmailResetPass.route)
                    })
                Spacer(modifier = Modifier.padding(5.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                dialogMessage = "Por favor, introduce email y contraseña."
                                showDialog = true
                                return@Button
                            }
                            appViewModel.setLoading(true)
                            authViewModel.signIn(email, password) { success, errorMessage ->
                                if (success) {
                                    navController.navigate(Screen.Home.route){
                                        popUpTo(Screen.Login.route) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    appViewModel.setLoading(false)
                                    dialogMessage= errorMessage.toString()
                                    showDialog = true
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    ) {
                        Text(text = "Ingresar",
                            color = White
                        )
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Row Icon",
                            tint = White
                        )
                    }
                }
            }
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "¿No tienes una cuenta?",color = Color.Black
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "Regístrate",
                        color = Primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Register.route)
                        }

                    )
                }

            }
        }
    }
        if (showDialog) {
            UserConfirmationDialog(
                onDismissRequest = { showDialog = false }
                , texto = dialogMessage
            )
        }
}

@Composable
fun UserConfirmationDialog(
    onDismissRequest: () -> Unit,
    texto: String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = BeigeClaro,
        title = {
            Text(
                text = texto,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                color = Color.Black
            )
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Cerrar", color = White)
            }
        },
        dismissButton = null
    )
}

