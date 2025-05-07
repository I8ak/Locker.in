package com.example.lockerin.presentation.viewmodel.lockers

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockerin.domain.model.Rental
import com.example.lockerin.domain.model.Tarjeta
import com.example.lockerin.domain.usecase.rental.AddRentalUseCase
import com.example.lockerin.domain.usecase.rental.CountRentalsByUserUseCase
import com.example.lockerin.domain.usecase.rental.DeleteRentalUseCase
import com.example.lockerin.domain.usecase.rental.GetRentalUseCase
import com.example.lockerin.domain.usecase.rental.ListRentalsByUserIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class RentalViewModel(
    val addRentalUseCase: AddRentalUseCase,
    val listRentalsByUserIdUseCase: ListRentalsByUserIdUseCase,
    val getRentalUseCase: GetRentalUseCase,
    val countLockersUseCase: CountRentalsByUserUseCase,
    val deleteRentalUseCase: DeleteRentalUseCase,
): ViewModel(){
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()
    private val _rentals: MutableStateFlow<Tarjeta?> = MutableStateFlow(null)
    val rentals: StateFlow<List<Rental>> = _userId
        .filterNotNull()
        .flatMapLatest { userId: String ->
            listRentalsByUserIdUseCase(userId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    @SuppressLint("SimpleDateFormat")

    fun setUserId(userId: String) {
        _userId.value = userId
    }


        @RequiresApi(Build.VERSION_CODES.O)
    fun isLockerAvailable(lockerId: String, date: Date?, city: String): Boolean {
//        if (date == null) return false
//
//        val lockersInCity = lockesrViewModel.lockers.value.filter {
//            it.city.equals(city, ignoreCase = true)
//        }
//
//        val selectedLocker = lockersInCity.find { it.lockerID == lockerId }
//        Log.d("casilleros", selectedLocker.toString())
//
//        if (selectedLocker == null) return false
//
//        if (selectedLocker.status) return true
//
//        return rentalLocker.value.any { rental ->
//            rental.lockerID == lockerId && rental.endDate?.before(date) == true
//        }
        return true
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addRental(rental: Rental) {
        viewModelScope.launch {
            addRentalUseCase(rental)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getRentalByUserId(userID: String): Rental? {
//        return rentalLocker.value.find { it.userID == userID }
        return Rental()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLockerByUserId(userID: String): String {
//        return rentalLocker.value.find { it.userID == userID }?.lockerID ?: ""
        return ""
    }

    private val _selectedRental = MutableStateFlow<Rental?>(null)
    val selectedRental: StateFlow<Rental?> = _selectedRental.asStateFlow()
    fun getRentalById(rentalId: String) {
        viewModelScope.launch {
            val rental = getRentalUseCase(rentalId)
            _selectedRental.value = rental
        }
    }

    private val _rentalCount = MutableStateFlow(0)
    val rentalCount: StateFlow<Int> = _rentalCount.asStateFlow()

    fun countRentals(userId: String) {
        viewModelScope.launch {
            val count = countLockersUseCase(userId)
            _rentalCount.value = count
        }
    }
    fun deleteRental(rental: Rental) {
        viewModelScope.launch {
            deleteRentalUseCase(rental)
        }
    }



}