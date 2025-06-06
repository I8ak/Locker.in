package com.example.lockerin.presentation.ui.components


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShopTwo
import androidx.compose.material.icons.filled.ViewCompactAlt
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lockerin.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lockerin.domain.model.User
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.example.lockerin.data.utils.NetworkUtils

import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun DrawerMenu(
    textoBar: String,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    lockersViewModel: LockersViewModel = koinViewModel(),
    fullUser: User?
) {
    // Estado del drawer de navegación (abierto/cerrado)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // Ancho del drawer, calculado como 2/3 del ancho de la pantalla
    val drawerWidth = with(LocalConfiguration.current) {
        screenWidthDp.dp * 2f / 3f
    }

    val context= LocalContext.current
    var showDialogConection by remember { mutableStateOf(false) }
    if (showDialogConection){
        NoConexionDialog(
            onDismiss = { showDialogConection = false }
        )
    }

    ModalNavigationDrawer(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .width(drawerWidth)
                    .fillMaxHeight()
                    .background(BeigeClaro)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable {
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.Account.createRoute(userID = fullUser?.userID.toString()))
                                } else {
                                    showDialogConection = true
                                }
                            }
                    ) {
                        val tipo = fullUser?.tipo
                        val name = fullUser?.name.orEmpty()

                        if (tipo == 0) {
                            val initial = name.firstOrNull()?.uppercaseChar() ?: '?'
                            val colorInt = fullUser?.avatar?.toInt() ?: 0
                            val avatarColor = Color(colorInt)

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(avatarColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initial.toString(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else if (tipo == 1) {
                            val context = LocalContext.current
                            val avatarResId = context.resources.getIdentifier(fullUser.avatar, "drawable", context.packageName)
                            Image(
                                painter = painterResource(id = avatarResId),
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                            )
                        }

                        // Muestra el nombre del usuario
                        Text(
                            text = name.ifBlank { "Nombre de Usuario" },
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    // Muestra el menú
                    DrawerItem(
                        icon = { Icon(Icons.Default.Home, "Inicio", tint = Color.Black) },
                        text = "Inicio",
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.Home.route) {
                                        launchSingleTop = true
                                    }
                                } else {
                                    showDialogConection = true
                                }
                            }
                        }
                    )
                    DrawerItem(
                        icon = { Icon(Icons.Default.ViewCompactAlt, "Reservas", tint = Color.Black) },
                        text = "Reservas",
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.ResrvedLockers.createRoute(userID = fullUser?.userID.toString())) {
                                        launchSingleTop = true
                                    }
                                } else {
                                    showDialogConection = true
                                }
                            }
                        }
                    )
                    DrawerItem(
                        icon = { Icon(Icons.Default.Person, "Cuenta", tint = Color.Black) },
                        text = "Cuenta",
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.Account.createRoute(userID = fullUser?.userID.toString())) {
                                        launchSingleTop = true
                                    }
                                } else {
                                    showDialogConection = true
                                }
                            }
                        }
                    )
                    DrawerItem(
                        icon = { Icon(Icons.Default.Map, "Mapa", tint = Color.Black) },
                        text = "Mapa",
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.MapScreen.route) {
                                        launchSingleTop = true
                                    }
                                } else {
                                    showDialogConection = true
                                }
                            }
                        }
                    )
                    DrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.informatioi),
                                contentDescription = "Información",
                                tint = Color.Black
                            )
                        },
                        text = "Información",
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.Information.route) {
                                        launchSingleTop = true
                                    }
                                } else {
                                    showDialogConection = true
                                }
                            }
                        }
                    )

                    // Log para depuración del rol del usuario
                    Log.e("DrawerMenu", "Role: $fullUser")

                    // Muestra el ítem "Lockers" solo si el usuario es administrador
                    if (fullUser?.role == "administrator") {
                        DrawerItem(
                            icon = { Icon(Icons.Default.ShopTwo, "Lockers", tint = Color.Black) },
                            text = "Lockers",
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    if (NetworkUtils.isInternetAvailable(context)) {
                                        navController.navigate(
                                            Screen.ListLockers.createRoute(fullUser.userID.toString())
                                        ) {
                                            launchSingleTop = true
                                        }
                                    } else {
                                        showDialogConection = true
                                    }
                                }
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BeigeClaro)
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    drawerState.close()
                                    if (NetworkUtils.isInternetAvailable(context)) {
                                        authViewModel.signOut(lockersViewModel)
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(0)
                                        }
                                    } else {
                                        showDialogConection = true
                                    }
                                }
                            }
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Cerrar Sesión",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Red
                        )
                    }
                }
            }
        },
        scrimColor = Color.Black.copy(alpha = 0.5f)
    ) {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize(),
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BeigeClaro)
                ) {
                    TopAppBar(
                        modifier = Modifier.align(Alignment.TopCenter),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = Color.Black,
                            navigationIconContentColor = Color.Black
                        ),
                        title = {
                            Text(
                                text = textoBar,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.Black,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        },
                        actions = {
                            Spacer(modifier = Modifier.width(48.dp))
                        }
                    )
                }
            },
            containerColor = BeigeClaro
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BeigeClaro)
            ) {
                content(innerPadding)
            }
        }
    }
}


@Composable
fun DrawerItem(
    icon: @Composable () -> Unit,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
    }
}