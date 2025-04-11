package com.example.lockerin.presentation.viewmodel.lockers

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.lockerin.domain.model.Rental
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class RentalViewModel: ViewModel(){
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    val format =  SimpleDateFormat("yyyy-MM-dd")
    @RequiresApi(Build.VERSION_CODES.O)
    private val _rentalLocker = MutableStateFlow<List<Rental>>(
        listOf(
            Rental(
                rentalID = "1",
                userID = "1",
                lockerID = "locker1",
                startDate =  Date(),
                endDate = format.parse("2025-04-8"),
                duration = 1
            ),
            Rental(
                rentalID = "2",
                userID = "1",
                lockerID = "locker2",
                startDate = Date(),  // Fecha actual
                endDate = format.parse("2025-05-01"),  // 1 mes después
                duration = 30
            ),
            Rental(
                rentalID = "3",
                userID = "3",
                lockerID = "locker3",
                startDate = Date(),  // Fecha pasada fija
                endDate = format.parse("2025-01-20"),
                duration = 5
            ),
            Rental(
                rentalID = "4",
                userID = "4",
                lockerID = "locker4",
                startDate = Date(),  // Fecha actual
                endDate = format.parse("2025-06-15"),
                duration = 7
            )
        )
    )
    @RequiresApi(Build.VERSION_CODES.O)
    val rentalLocker:StateFlow<List<Rental>> = _rentalLocker

    @RequiresApi(Build.VERSION_CODES.O)
    fun countLockers(userId: String): Int {
        var count = 0
        for (rental in rentalLocker.value) {
            if (rental.userID == userId) {
                count++
            }
        }
        return count
    }
}