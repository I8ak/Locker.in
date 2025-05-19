package com.example.lockerin.presentation.ui.screens.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.toString

@Composable
fun ConfigurationScreen(
    userId: String,
    navController: NavHostController,
    userViewModel: UsersViewModel= koinViewModel(),
    authViewModel: AuthViewModel=koinViewModel()
){
    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }
    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    val user=userViewModel.getUserById(userId.toString())
    DrawerMenu(
      textoBar = "Información",
        navController=navController,
        authViewModel = viewModel(),
        fullUser = userState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
                PrivacitySecurity()
                Spacer(modifier = Modifier.padding(8.dp))
                Help()
                Spacer(modifier = Modifier.padding(8.dp))
                AboutUs()
                Spacer(modifier = Modifier.padding(8.dp))
                ContactUs()
            }

        }
    )
}

@Composable
fun PrivacitySecurity(){
    var isSelected by remember { mutableStateOf(false) }
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
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Privacidad y seguridad",
                modifier = Modifier.clickable {
                    isSelected = !isSelected
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.PrivacyTip,
                contentDescription = "Privacidad y seguridad",
                tint = Color.Black
            )
        }

        if (isSelected) {
            Spacer(modifier = Modifier.padding(4.dp))
            Text("1. Proteccion de datos personales", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Todos los datos que el usuario proporciona al registrarse o al utilizar la aplicación (como nombre, correo electrónico, método de pago, etc.) son almacenados de forma segura y nunca se comparten con terceros sin consentimiento explícito. Usamos bases de datos cifradas y cumplimos con el Reglamento General de Protección de Datos (RGPD).", fontSize = 13.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(8.dp))
            Text("2. Seguridad en las Transacciones", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Las operaciones de pago y reserva se realizan mediante plataformas de pago seguras y certificadas. La información bancaria está protegida con encriptación de extremo a extremo, garantizando que ninguna persona ajena pueda acceder a tus datos financieros.", fontSize = 13.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(8.dp))
            Text("3. Control y Monitoreo de Lockers", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Cada locker cuenta con un sistema de seguimiento de estado en tiempo real. Solo los usuarios autorizados pueden acceder a su contenido durante el período reservado. Además, se generan registros automáticos de cada acceso para asegurar un historial claro y transparente de uso.", fontSize = 13.sp, color = Color.Black)
        }
    }
}

@Composable
fun Help(){
    var isSelected by remember { mutableStateOf(false) }
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
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Ayuda",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.Help,
                contentDescription = "Ayuda",
                tint = Color.Black,
            )
        }

        if (isSelected) {
            Spacer(modifier = Modifier.padding(4.dp))
            Text("1. ¿Cómo reservo un locker?", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Selecciona la ciudad, la fecha y el rango de horas en los que necesitas guardar tus pertenencias. La app te mostrará los lockers disponibles según tus criterios. Una vez elegido, pulsa en “Reservar” y sigue los pasos para confirmar el pago.", fontSize = 12.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(8.dp))
            Text("2. ¿Cómo accedo a mi locker reservado?", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Cuando llegue la hora de tu reserva, dirígete al punto físico del locker. Desde la app, entra en tu reserva activa y pulsa “Abrir Locker”. El sistema se encargará de desbloquearlo automáticamente si estás en el lugar correcto y dentro del horario establecido.", fontSize = 12.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(8.dp))
            Text("3. ¿Qué hago si tengo un problema o duda?", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Puedes contactar con nuestro equipo de soporte desde la sección “Contactanos” en el menú principal. También puedes escribirnos por correo o usar el chat de ayuda disponible en la app. Te responderemos lo antes posible para ayudarte con lo que necesites.", fontSize = 12.sp, color = Color.Black)
        }
    }
}

@Composable
fun AboutUs(){
    var isSelected by remember { mutableStateOf(false) }
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
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Sobre nosotros",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Informacion",
                tint = Color.Black,
            )
        }

        if (isSelected) {
            Spacer(modifier = Modifier.padding(4.dp))
            Text("1. ¿Quiénes somos?", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Somos un equipo joven con pasión por la tecnología y los viajes. Nuestra experiencia en desarrollo de software y conocimiento del mercado turístico nos permite ofrecer una plataforma moderna y útil para cualquier tipo de viajero.", fontSize = 12.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(8.dp))
            Text("2. ¿Qué hacemos?", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Conectamos a usuarios con espacios seguros donde pueden guardar sus pertenencias temporalmente. Desde mochilas hasta maletas, nuestros lockers están distribuidos estratégicamente en puntos clave de la ciudad, siempre disponibles a través de una app intuitiva.", fontSize = 12.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(8.dp))
            Text("3. ¿Por qué lo hacemos?", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Sabemos lo incómodo que es cargar con maletas mientras esperas un check-in o haces turismo. Locker.in nació para resolver ese problema y ayudarte a disfrutar de tu tiempo sin preocupaciones, con la confianza de que tus objetos están seguros.", fontSize = 12.sp, color = Color.Black)
        }
    }
}

@Composable
fun ContactUs(){
    var isSelected by remember { mutableStateOf(false) }
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
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Contactanos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.ContactSupport,
                contentDescription = "Contact us",
                tint = Color.Black
            )
        }

        if (isSelected) {
            Spacer(modifier = Modifier.padding(4.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Correo electrónico:", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text("soporte@locker-in.com", fontSize = 15.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.padding(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Teléfono:", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text("+34 654 321 987", fontSize = 15.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.padding(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Dirección:", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text("Calle del Viajero 42, 28001 Madrid, España", fontSize = 15.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.padding(8.dp))

            Text("Nuestro equipo de atención al cliente está disponible de lunes a domingo, de 9:00 a 21:00 (hora local). \nTe responderemos lo antes posible. \n¡Gracias por confiar en Locker.in!", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
        }
    }
}


