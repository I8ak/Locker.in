package com.example.lockerin.presentation.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lockerin.data.utils.NetworkUtils
import com.example.lockerin.presentation.ui.components.NoConexionDialog
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailResetPassScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    // Variables de estado
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val context= LocalContext.current
    var showDialogConection by remember { mutableStateOf(false) }
    if (showDialogConection){
        NoConexionDialog(
            onDismiss = { showDialogConection = false }
        )
    }

    // Diseño de la interfaz de usuario
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BeigeClaro,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                ),
                title = {
                    Text(
                        text = "Resetear contraseña",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Ir atras",
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
            )
        },
        containerColor = BeigeClaro
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email", color = Color.Black) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = Color.Black
                    )
                },
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
            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = {
                    if (NetworkUtils.isInternetAvailable(context)) {
                        if (email.isNotEmpty()) {
                            authViewModel.sendPasswordResetEmail(email) { isSuccess, errorMessage ->
                                if (isSuccess) {
                                    showDialog = true
                                    dialogMessage = "Enlace enviado, revisa tu correo"
                                } else {
                                    showDialog = true
                                    dialogMessage = "Error al enviar el enlace: $errorMessage"
                                }
                            }
                        } else {
                            showDialog = true
                            dialogMessage = "Por favor, introduce tu email para resetear la contraseña."
                        }
                    } else {
                        showDialogConection = true
                    }


                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
            ) {
                Text(
                    text = "Enviar enlace",
                    color = White
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Icono de flecha",
                    tint = White
                )
            }
        }
        // Diálogo para mostrar mensajes al usuario
        if (showDialog) {
            ConfirmationDialog(
                onDismissRequest = { showDialog = false },
                texto = dialogMessage
            )
        }
    }
}

@Composable
fun ConfirmationDialog(
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