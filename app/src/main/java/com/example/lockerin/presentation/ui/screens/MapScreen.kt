import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.lockerin.presentation.navigation.Screen
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(navController: NavHostController) {
    // Manejo del botón de retroceso para volver a la pantalla de inicio
    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }

    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    // Launcher para solicitar permisos de ubicación
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            getUserLocation(context) {
                userLocation = it
            }
        }
    }

    // Efecto lanzado al iniciar el Composable para verificar y solicitar permisos de ubicación
    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            getUserLocation(context) {
                userLocation = it
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Estado de la posición de la cámara del mapa, usando la ubicación del usuario si está disponible
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation ?: LatLng(40.4168, -3.7038), 12f)
    }

    // Componente GoogleMap
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = userLocation != null
        )
    ) {
        // Marcadores para los lockers en Madrid
        Marker(
            state = MarkerState(position = LatLng(40.4203, -3.7058)),
            title = "Locker Gran Vía",
            snippet = "Calle Gran Vía, 32, 28013 Madrid"
        )
        Marker(
            state = MarkerState(position = LatLng(40.4193, -3.7044)),
            title = "Locker Preciados",
            snippet = "Calle Preciados, 10, 28013 Madrid"
        )
        Marker(
            state = MarkerState(position = LatLng(40.4151, -3.7004)),
            title = "Locker Atocha",
            snippet = "Calle de Atocha, 58, 28012 Madrid"
        )
        Marker(
            state = MarkerState(position = LatLng(40.4175, -3.7048)),
            title = "Locker Montera",
            snippet = "Calle de la Montera, 24, 28013 Madrid"
        )
        Marker(
            state = MarkerState(position = LatLng(40.4208, -3.7069)),
            title = "Locker Fuencarral",
            snippet = "Calle de Fuencarral, 45, 28004 Madrid"
        )
        // Marcadores para los lockers en Barcelona
        Marker(
            state = MarkerState(position = LatLng(41.3790, 2.1406)),
            title = "Locker Sants",
            snippet = "Carrer de Sants, 63, 08014 Barcelona"
        )
        Marker(
            state = MarkerState(position = LatLng(41.3870, 2.1700)),
            title = "Locker Plaça Catalunya",
            snippet = "Plaça de Catalunya, 1, 08002 Barcelona"
        )
        Marker(
            state = MarkerState(position = LatLng(41.3884, 2.1621)),
            title = "Locker Pelai",
            snippet = "Carrer de Pelai, 13, 08001 Barcelona"
        )
        Marker(
            state = MarkerState(position = LatLng(41.3753, 2.1491)),
            title = "Locker Tarragona",
            snippet = "Carrer de Tarragona, 108, 08015 Barcelona"
        )
    }
}

@SuppressLint("MissingPermission")
fun getUserLocation(context: android.content.Context, onLocationReady: (LatLng) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocationReady(LatLng(location.latitude, location.longitude))
        }
    }
}