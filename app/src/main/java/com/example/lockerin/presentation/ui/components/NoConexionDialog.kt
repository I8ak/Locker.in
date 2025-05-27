package com.example.lockerin.presentation.ui.components

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lockerin.data.utils.NetworkUtils
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import kotlinx.coroutines.launch

@Composable
fun NoConexionDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        containerColor = BeigeClaro,
        title = {
            Text(
                "Sin conexión a Internet",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                "Por favor, comprueba tu conexión.",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                color = Color.Black,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick =onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Cerrar", color = White)
            }
        }
    )
}