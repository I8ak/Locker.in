package com.example.lockerin.presentation.ui.screens.user

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.LoadingScreen
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountScreen(
    userID: String,
    navController: NavHostController,
    userViewModel: UsersViewModel = koinViewModel(),
    authViewModel: AuthViewModel = viewModel(),
    lockersViewModel: LockersViewModel = koinViewModel()
) {
    // Obtiene el ID del usuario actual.
    val userId = authViewModel.currentUserId

    // Maneja el botón de retroceso para navegar a la pantalla de inicio.
    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }

    // Si el ID de usuario es nulo, redirige a la pantalla de inicio de sesión.
    if (userId == null) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
            }
        }
        return
    }

    // Recopila el estado del usuario del ViewModel.
    val userState by userViewModel.user.collectAsState()
    // Obtiene los datos del usuario por su ID.
    userViewModel.getUserById(userId.toString())
    Log.d("usuario", userId.toString())

    // Estado para controlar la pantalla de carga.
    var isLoading by remember { mutableStateOf(true) }

    // Simula un retraso para la pantalla de carga.
    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
    }

    // Muestra la pantalla de carga o el contenido principal.
    if (isLoading) {
        LoadingScreen(isLoading = isLoading)
    } else {
        DrawerMenu(
            textoBar = "Cuenta",
            navController = navController,
            authViewModel = authViewModel,
            fullUser = userState,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it)
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    // Muestra el nombre de usuario
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Usuario",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .weight(0.3f)
                                .height(30.dp),
                            fontSize = 20.sp,
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = userState?.name.toString(),
                            modifier = Modifier
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .weight(0.7f)
                                .height(30.dp),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                    // Muestra el email del usuario
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Email",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .weight(0.3f)
                                .height(30.dp),
                            fontSize = 20.sp,
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = userState?.email.toString(),
                            modifier = Modifier
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .weight(0.7f)
                                .height(30.dp),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    ChangePass(authViewModel)
                    Spacer(modifier = Modifier.padding(8.dp))
                    DeleteAcount(authViewModel, navController, userViewModel, lockersViewModel, userId.toString())
                    Spacer(modifier = Modifier.padding(8.dp))
                    Cards(userId.toString(), navController)
                    Spacer(modifier = Modifier.padding(8.dp))
                    AvatarChoose(userId.toString(), navController)
                }
            }
        )
    }
}

@Composable
fun ChangePass(authViewModel: AuthViewModel) {
    var isSelected by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassw by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordsMatch by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val oldPassFocusRequester = remember { FocusRequester() }
    val newPasswordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable {
                isSelected = !isSelected
            }
    ) {
        Text(
            text = "Cambiar contraseña",
            modifier = Modifier.clickable {
                isSelected = !isSelected
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        if (isSelected) {
            OutlinedTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = { Text(text = "Introduce la antigua contraseña", color = Color.Black) },
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
                    .background(Color.Transparent, RoundedCornerShape(12.dp))
                    .focusRequester(oldPassFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        newPasswordFocusRequester.requestFocus()
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
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newPassw,
                onValueChange = { newPassw = it },
                label = { Text(text = "Introduce la nueva contraseña", color = Color.Black) },
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
                    .background(Color.Transparent, RoundedCornerShape(12.dp))
                    .focusRequester(newPasswordFocusRequester),
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
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPass,
                onValueChange = { confirmPass = it },
                label = { Text(text = "Confirmar contraseña", color = Color.Black) },
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
            passwordsMatch = newPassw == confirmPass
            if (!passwordsMatch) {
                Text(
                    text = "Las contraseñas no coinciden",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        if (oldPassword.isEmpty() || newPassw.isEmpty() || confirmPass.isEmpty()) {
                            dialogText = "Todos los campos deben estar llenos."
                            showDialog = true
                        } else if (newPassw.length < 6) {
                            dialogText = "La nueva contraseña debe tener al menos 6 caracteres."
                            showDialog = true
                        } else if (!passwordsMatch) {
                            dialogText = "Las contraseñas no coinciden."
                            showDialog = true
                        } else {
                            authViewModel.updatePassword(oldPassword, newPassw) { success, errorMessage ->
                                if (success) {
                                    oldPassword = ""
                                    newPassw = ""
                                    confirmPass = ""
                                    dialogText = "Contraseña cambiada exitosamente."
                                    isSelected = false
                                } else {
                                    dialogText = errorMessage ?: "Error al cambiar la contraseña."
                                }
                                showDialog = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                ) {
                    Text(text = "Cambiar", color = White)
                }
            }
        }

        if (showDialog) {
            PasswordChangeConfirmationDialog(
                onDismissRequest = { showDialog = false },
                texto = dialogText
            )
        }
    }
}

@Composable
fun DeleteAcount(
    authViewModel: AuthViewModel,
    navController: NavController,
    userViewModel: UsersViewModel,
    lockersViewModel: LockersViewModel,
    userId: String
) {
    var showDialog by remember { mutableStateOf(false) }
    var deleteErrorMessage by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Eliminar cuenta",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.PersonRemove,
            contentDescription = "Eliminar cuenta",
            tint = Color.Black,
            modifier = Modifier.clickable {
                showDialog = true
            }
        )
        if (showDialog) {
            ConfirmDeleteAccountDialog(
                onConfirmation = {
                    // Primero intenta borrar los datos del usuario de la base de datos
                    userViewModel.deleteAccount(userId) { success, errorMessage ->
                        if (success) {
                            Log.d("DeleteAccount", "Datos de usuario eliminados de la base de datos.")
                            // Si se borran los datos, intenta borrar la cuenta de autenticación
                            authViewModel.deleteUser { success2, errorMessage2 ->
                                if (success2) {
                                    Log.d("DeleteAccount", "Cuenta de autenticación eliminada.")
                                    // Si se borra la cuenta de autenticación, cierra la sesión y navega
                                    authViewModel.signOut(lockersViewModel = lockersViewModel)
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                } else {
                                    deleteErrorMessage = errorMessage2 ?: "Error al eliminar la cuenta de autenticación."
                                    Log.e("DeleteAccount", "Error al eliminar la cuenta de autenticación: $deleteErrorMessage")
                                }
                            }
                        } else {
                            deleteErrorMessage = errorMessage ?: "Error al eliminar los datos del usuario de la base de datos."
                            Log.e("DeleteAccount", "Error al eliminar datos de usuario de la base de datos: $deleteErrorMessage")
                        }
                    }
                },
                onDismissRequest = {
                    showDialog = false
                },
                errorMessage = deleteErrorMessage
            )
        }
    }
}

@Composable
fun ConfirmDeleteAccountDialog(
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    errorMessage: String? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = BeigeClaro,
        title = {
            Text(
                text = "¿Eliminar cuenta?",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "¿Quieres eliminar tu cuenta de forma permanente? Esta acción es irreversible y borrará todos tus datos.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    color = Color.Black,
                    fontSize = 14.sp
                )
                errorMessage?.let {
                    Text(
                        text = "Error: $it",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        onConfirmation()
                    },
                    modifier = Modifier.weight(1f).padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Sí", color = White)
                }
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f).padding(start = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("No", color = White)
                }
            }
        },
        dismissButton = null
    )
}

@Composable
fun PasswordChangeConfirmationDialog(
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

@Composable
fun Cards(
    userID: String,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable {
                navController.navigate(Screen.Cards.createRoute(userID))
            }
    ) {
        Text(
            text = "Tarjetas asociadas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.CreditCard,
            contentDescription = "Tarjetas",
            tint = Color.Black,
            modifier = Modifier.clickable {
                navController.navigate(Screen.Cards.createRoute(userID))
            }
        )
    }
}

@Composable
fun AvatarChoose(
    userID: String,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable {
                navController.navigate(Screen.ChooseAvatar.createRoute(userID))
            }
    ) {
        Text(
            text = "Elegir avatar",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.Person4,
            contentDescription = "Elegir avatar",
            tint = Color.Black
        )
    }
}