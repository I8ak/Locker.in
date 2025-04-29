package com.example.lockerin.presentation.viewmodel.lockers

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.lockerin.domain.model.Rental
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class RentalViewModel: ViewModel(){
    val lockesrViewModel = LockersViewModel()
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
                endDate = format.parse("2025-05-8"),
            ),
            Rental(
                rentalID = "2",
                userID = "1",
                lockerID = "locker2",
                startDate = Date(),  // Fecha actual
                endDate = format.parse("2025-05-01"),  // 1 mes después
            ),
//            Rental(
//                rentalID = "3",
//                userID = "3",
//                lockerID = "locker3",
//                startDate = Date(),  // Fecha pasada fija
//                endDate = format.parse("2025-01-20"),
//            ),
//            Rental(
//                rentalID = "4",
//                userID = "4",
//                lockerID = "locker4",
//                startDate = Date(),  // Fecha actual
//                endDate = format.parse("2025-06-15"),
//            )
        )
    )
    @RequiresApi(Build.VERSION_CODES.O)
    val rentalLocker:StateFlow<List<Rental>> = _rentalLocker

    @RequiresApi(Build.VERSION_CODES.O)
    fun countLockers(userId: String): Int {
        return rentalLocker.value.count { it.userID == userId }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun isLockerAvailable(lockerId: String, date: Date?, city: String): Boolean {
        if (date == null) return false

        val lockersInCity = lockesrViewModel.lockers.value.filter {
            it.city.equals(city, ignoreCase = true)
        }

        val selectedLocker = lockersInCity.find { it.lockerID == lockerId }
        Log.d("casilleros", selectedLocker.toString())

        if (selectedLocker == null) return false

        if (selectedLocker.status) return true

        return rentalLocker.value.any { rental ->
            rental.lockerID == lockerId && rental.endDate?.before(date) == true
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addRental(rental: Rental) {
        _rentalLocker.value = _rentalLocker.value + rental
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRentalById(rentalID: String): Rental? {
        return rentalLocker.value.find { it.rentalID == rentalID }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getRentalByUserId(userID: String): Rental? {
        return rentalLocker.value.find { it.userID == userID }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLockerByUserId(userID: String): String {
        return rentalLocker.value.find { it.userID == userID }?.lockerID ?: ""
    }



}