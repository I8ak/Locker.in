package com.example.lockerin.data.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.lockerin.R

object Notifications {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification(context: Context, title: String, message: String) {
        val channelId = "reservation_channel"

        // Crear el canal solo si es Android 8 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reservas"
            val descriptionText = "Notificaciones de reservas"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        // Crear la notificación
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.logo_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(receiver = NotificationManagerCompat.from(context))
         {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}