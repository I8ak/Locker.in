package com.example.lockerin.presentation.ui.screens.user

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.viewmodel.users.AuthState
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var user by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf(false) }
    var confirmFieldFocused by remember { mutableStateOf(false) }
    var lengthPass by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val userFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }



    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BeigeClaro)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
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
                        )
                        .focusRequester(userFocusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            emailFocusRequester.requestFocus()
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    singleLine = true
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
                        )
                        .focusRequester(emailFocusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            passwordFocusRequester.requestFocus()
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    singleLine = true
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
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .focusRequester(passwordFocusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            confirmPasswordFocusRequester.requestFocus()
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    singleLine = true
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
                        }
                        .focusRequester(confirmPasswordFocusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    singleLine = true
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
                if (!confirmFieldFocused && !confirmPassword && password.length < 6 && passwordConfirm.length < 6) {
                    Text(
                        text = "Las contraseñas tienen menos de 6 dígitos",
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
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    "https://www.aepd.es/politica-de-privacidad-y-aviso-legal".toUri()
                                )
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
                            val userRole = "user"
                            if (allFieldsValid) {
                                authViewModel.createUser(
                                    user.capitalize(),
                                    email,
                                    password,
                                    userRole
                                ) { success, errorMessage ->
                                    if (success) {
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Register.route) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Error al registrar el usuario: $errorMessage",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
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
                                        if (lengthPass) appendLine("• Las contraseñas tiene menos de 6 dígitos")
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
                            containerColor = if (allFieldsValid) Primary else Color.Gray,
                            disabledContentColor = Color.White.copy(alpha = 0.7f),
                            disabledContainerColor = Color.Gray
                        ),
                        enabled = true
                    ) {
                        Text("Registrarse", color = Color.White)
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Registrarse",
                            tint = Color.White
                        )

                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¿Ya tienes una cuenta?", color = Color.Black
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "Login",
                        color = Primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                        }
                    )
                }
            }
        }
    )
}

