package com.example.lockerin.presentation.ui.components

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
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
import androidx.compose.material.icons.filled.Cases
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShopTwo
import androidx.compose.material.icons.filled.ViewCompactAlt
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.div
import kotlin.times

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
//    val currentUser by authViewModel.currentUserFlow.collectAsState()
    val currentUserID = authViewModel.currentUserId
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val defaultAvatar = painterResource(R.drawable.default_avatar)
    val drawerWidth = with(LocalConfiguration.current) {
        screenWidthDp.dp * 2f / 3f
    }
    val usersViewModel:UsersViewModel = koinViewModel()

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
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Image(
                            painter = defaultAvatar,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Transparent, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = fullUser?.name ?: "Nombre de Usuario",
                            modifier = Modifier.padding(start = 8.dp).weight(1f),
                            color = Color.Black
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
                        onClick = { navController.navigate(Screen.ResrvedLockers.route) }
                    )
                    DrawerItem(
                        icon = { Icon(Icons.Default.Person, "Cuenta", tint = Color.Black) },
                        text = "Cuenta",
                        onClick = { navController.navigate(Screen.Acount.createRoute(userID = fullUser?.userID.toString())) }
                    )
                    DrawerItem(
                        icon = { Icon(Icons.Default.Settings, "Configuración", tint = Color.Black) },
                        text = "Configuración",
                        onClick = {navController.navigate(Screen.Configuration.route) }
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
            style = MaterialTheme.typography.titleMedium
            ,color = Color.Black
        )
    }
}

//@Preview
//@Composable
//fun PreviewDrawerMenu() {
//    DrawerMenu(
//        textoBar = "Drawer Menu",
//        navController = rememberNavController(),
//        authViewModel = viewModel(),
//        content = {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(it)
//            ) {
//                Text(text = "Contenido de la pantalla")
//            }
//        }
//    )
//}