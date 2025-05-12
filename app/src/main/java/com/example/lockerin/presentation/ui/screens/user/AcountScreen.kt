package com.example.lockerin.presentation.ui.screens.user

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AcountScreen(
    userID: String,
    navController: NavHostController,
    userViewModel: UsersViewModel= koinViewModel(),
    authViewModel: AuthViewModel = viewModel()
){
    val userId= authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    val user=userViewModel.getUserById(userID)
    Log.d("usuario",userID)
    DrawerMenu(
        textoBar = "Cuenta",
        navController=navController,
        authViewModel = viewModel(),
        fullUser = userState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
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
                        text = user?.name.toString() ,
                        modifier = Modifier
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                            .weight(0.7f)
                            .height(30.dp),
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
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
                        text = user?.email.toString(),
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
                DeleteAcount(authViewModel,navController,userViewModel,userId.toString())
                Spacer(modifier = Modifier.padding(8.dp))
                Cards(userID,navController)


            }
        }
    )
}

@Composable
fun ChangePass(authViewModel: AuthViewModel) {
    var isSelected by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassw by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordsMatch by remember { mutableStateOf(true) } // Estado para verificar si las contraseñas coinciden
    var showDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo
    var texto by remember { mutableStateOf("Contraseña cambiada") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable{
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
                label = {
                    Text(
                        text = "Introduce la antigua contraseña",
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
            OutlinedTextField(
                value = newPassw,
                onValueChange = { newPassw = it },
                label = {
                    Text(
                        text = "Introduce la nueva contraseña",
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

            OutlinedTextField(
                value = confirmPass,
                onValueChange = { confirmPass = it },
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
                    .background(Color.Transparent),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.padding(5.dp))
            if (newPassw != confirmPass) {
                passwordsMatch = false
            } else {
                passwordsMatch = true
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        showDialog = true
                        if (oldPassword.isEmpty() && newPassw.isEmpty() && confirmPass.isEmpty() ){
                            texto="Los campos estan vacios"
                        }else {
                            if (passwordsMatch){
                                authViewModel.updatePassword(newPassw) { success, errorMessage ->
                                    if (success) {
                                        texto = "Contraseña cambiada exitosamente."
                                        oldPassword = ""
                                        newPassw = ""
                                        confirmPass = ""
                                    }
                                }
                            }else {
                                texto="Las contraseñas no coinciden"
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
                texto = texto
            )
        }
    }
}

@Composable
fun DeleteAcount(authViewModel: AuthViewModel, navController: NavController,userViewModel: UsersViewModel,userId:String){
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
            imageVector = Icons.Default.DeleteForever,
            contentDescription = "delete",
            tint = Color.Black,
            modifier = Modifier.clickable {
                showDialog=true
            }
        )
        if (showDialog) {
            ConfirmDeleteAccountDialog(
                onConfirmation = {
                    userViewModel.deleteAccount(userId) { success, errorMessage ->
                        if (success) {
                            authViewModel.deleteUser { success, errorMessage ->
                                authViewModel.signOut()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        } else {
                            deleteErrorMessage = errorMessage
                        }
                    }


                },
                onDismissRequest = {
                    showDialog = false // Cerrar diálogo al cancelar
                }
            )
        }
    }
}

@Composable
fun ConfirmDeleteAccountDialog(
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
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
                )
            },
            text = {
                Text(
                    text = "¿Quieres eliminar tu cuenta de forma permanente?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    color = Color.Black,
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            onConfirmation()
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Sí", color = White)
                    }
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
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
    texto:String
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
fun Cards(userID: String,
          navController: NavHostController){
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
            contentDescription = "Cards",
            tint = Color.Black,
            modifier = Modifier.clickable {
                navController.navigate(Screen.Cards.createRoute(userID))
            }
        )
    }

}
@Preview
@Composable
fun CountScreenPReview(){
    AcountScreen("1",
        navController = rememberNavController()
    )
}