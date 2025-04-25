package com.example.lockerin.presentation.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.presentation.ui.theme.Primary
import androidx.core.net.toUri
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var user by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf(false) }
    var confirmFieldFocused by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState)},
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(BeigeClaro)
                    .padding(horizontal = 16.dp, vertical = 30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { navController.popBackStack() }
                        .size(40.dp)
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BeigeClaro)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Crear cuenta",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.padding(20.dp))

                    OutlinedTextField(
                        value = user,
                        onValueChange = { user = it },
                        label = {
                            Text(
                                text = "Usuario",
                                color = Color.Black
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Person Icon",
                                tint = Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Transparent,
                                RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black
                        )

                    )
                    Spacer(modifier = Modifier.padding(5.dp))

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
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon",
                                tint = Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Transparent,
                                RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.padding(5.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(
                                text = "Contraseña",
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
                        )
                    )
                    Spacer(modifier = Modifier.padding(5.dp))

                    OutlinedTextField(
                        value = passwordConfirm,
                        onValueChange = { passwordConfirm = it },
                        label = {
                            Text(
                                text = "Confirmar contraseña",
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
                            .background(Color.Transparent)
                            .onFocusChanged { focusState ->
                                confirmFieldFocused = focusState.isFocused
                                if (!focusState.isFocused) {
                                    confirmPassword = password == passwordConfirm
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                        )
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    if (!confirmFieldFocused && !confirmPassword && passwordConfirm.isNotEmpty()) {
                        Text(
                            text = "Las contraseñas no coinciden",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    var isChecked by remember { mutableStateOf(false) }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it },
                            Modifier.size(20.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = Primary,
                                uncheckedColor = Color.Black
                            )

                        )
                        val context = LocalContext.current

                        Text(
                            text = "Acepto los términos y condiciones",
                            color = Color.Black,
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW,
                                        "https://www.aepd.es/politica-de-privacidad-y-aviso-legal".toUri())
                                    context.startActivity(intent)
                                },
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    val allFieldsValid = user.isNotEmpty() &&
                            email.isNotEmpty() &&
                            password.isNotEmpty() &&
                            passwordConfirm.isNotEmpty() &&
                            password == passwordConfirm &&
                            isChecked

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                if (allFieldsValid) {
                                    navController.navigate(Screen.Home.route){
                                        popUpTo(Screen.Register.route) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    scope.launch {
                                        val errorMessage = buildString {
                                            if (user.isEmpty()) appendLine("• Usuario requerido")
                                            if (email.isEmpty()) appendLine("• Email requerido")
                                            if (password.isEmpty()) appendLine("• Contraseña requerida")
                                            if (passwordConfirm.isEmpty()) appendLine("• Confirmar contraseña")
                                            if (password != passwordConfirm) appendLine("• Las contraseñas no coinciden")
                                            if (!isChecked) appendLine("• Debes aceptar los términos")
                                        }
                                        snackbarHostState.showSnackbar(
                                            message = errorMessage.ifEmpty { "Complete todos los campos" },
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (allFieldsValid) Primary else Color.Gray.copy(alpha = 0.5f),
                                disabledContentColor = Color.White.copy(alpha = 0.7f)
                            ),
                            enabled = true
                        ) {
                            Text("Ingresar", color = Color.White)
                            Icon(Icons.Default.ArrowForward, contentDescription = "Ingresar",
                                tint = Color.White)
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "¿Ya tienes una cuenta?"
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "Login",
                        color = Primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Login.route)
                        }

                    )
                }

            }
        }
    )
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController =
        rememberNavController())
}