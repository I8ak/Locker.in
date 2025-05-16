package com.example.lockerin.data.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.lockerin.data.source.remote.RentalFirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.ZoneId

class RentalReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        val firestore = FirebaseFirestore.getInstance()
        val rentalsRepository = RentalFirestoreRepository(firestore)
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return Result.success()

        val rentals = rentalsRepository.getRentalByUserId(currentUser.uid).first()

        val now = LocalDateTime.now()
        val in10Minutes = now.plusMinutes(10)

        rentals.forEach { rental ->
            val endTime = rental.endDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
            if (endTime != null &&
                endTime.isAfter(now) &&
                endTime.isBefore(in10Minutes)) {

                Notifications.showNotification(
                    context = applicationContext,
                    title = "Reserva por finalizar",
                    message = "Tu reserva finaliza en 10 minutos."
                )
                return@forEach
            }
        }

        return Result.success()
    }


}