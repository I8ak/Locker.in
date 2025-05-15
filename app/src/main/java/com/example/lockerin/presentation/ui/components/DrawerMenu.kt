package com.example.lockerin.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lockerin.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lockerin.domain.model.User
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerMenu(
    textoBar: String,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit,
    authViewModel: AuthViewModel=viewModel(),
    fullUser:User?
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val drawerWidth = with(LocalConfiguration.current) {
        screenWidthDp.dp * 2f / 3f
    }

    ModalNavigationDrawer(
        modifier = Modifier.statusBarsPadding(),
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
                                navController.navigate(Screen.Acount.createRoute(userID = fullUser?.userID.toString()))
                            }
                    ) {
                        val name = fullUser?.name.orEmpty()
                        val initial = name.firstOrNull()?.uppercaseChar() ?: "?"
                        val avatarColor = remember(fullUser?.userID) { randomColor() }

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


                    // Items del menú
                    DrawerItem(
                        icon = { Icon(Icons.Default.Home, "Inicio", tint = Color.Black) },
                        text = "Inicio",
                        onClick = { navController.navigate(Screen.Home.route) }
                    )
                    DrawerItem(
                        icon = { Icon(Icons.Default.ViewCompactAlt, "Reservas", tint = Color.Black) },
                        text = "Reservas",
                        onClick = { navController.navigate(Screen.ResrvedLockers.createRoute(userID = fullUser?.userID.toString())) }
                    )
                    DrawerItem(
                        icon = { Icon(Icons.Default.Person, "Cuenta", tint = Color.Black) },
                        text = "Cuenta",
                        onClick = { navController.navigate(Screen.Acount.createRoute(userID = fullUser?.userID.toString())) }
                    )
                    DrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.informatioi),
                                contentDescription = "Informacion",
                                tint = Color.Black
                            )
                        },
                        text = "Información",
                        onClick = { navController.navigate(Screen.Information.route) }
                    )
                    if (fullUser?.role == "administrator"){
                        DrawerItem(
                            icon = { Icon(Icons.Default.ShopTwo, "Lockers", tint = Color.Black) },
                            text = "Lockers",
                            onClick = { navController.navigate(Screen.ListLockers.createRoute(
                                fullUser.userID.toString())) }
                        )

                    }
                }

                // Barra inferior fija
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BeigeClaro)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    drawerState.close()
                                    authViewModel.signOut()
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Login.route) {
                                            inclusive = true
                                        }
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
                Box(modifier = Modifier
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
                        title={
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
                                onClick = {  scope.launch { drawerState.open() } }
                            ) {
                                Icon(imageVector = Icons.Default.Menu
                                    ,contentDescription = "Menu"
                                    ,tint = Color.Black,
                                    modifier = Modifier.size(30.dp))
                            }

                        },
                        actions = {
                            Spacer(modifier = Modifier.width(48.dp))
                        }
                    )
                }

            }, containerColor = BeigeClaro
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
fun randomColor(): Color {
    val colors = listOf(
        Color(0xFFE57373),
        Color(0xFF64B5F6),
        Color(0xFF81C784),
        Color(0xFF673AB7),
        Color(0xFFBA68C8),
        Color(0xFFFF8A65)
    )
    return colors.random()
}