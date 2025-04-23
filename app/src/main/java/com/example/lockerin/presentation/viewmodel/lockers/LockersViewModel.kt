package com.example.lockerin.presentation.viewmodel.lockers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockerin.domain.model.Locker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LockersViewModel:ViewModel() {
    private val _lockers = MutableStateFlow<List<Locker>>(listOf(
        Locker(
            lockerID = "locker1",
            location = "Location 1",
            city = "Madrid",
            status = true,
            size = "Large",
            dimension = "4x4x4",
            pricePerHour = 10.0,
        ),
        Locker(
            lockerID = "locker2",
            location = "Location 2",
            city = "Madrid",
            status = false,
            size = "Medium",
            dimension = "3x3x3",
            pricePerHour = 5.0,
        ),
        Locker(
            lockerID = "locker3",
            location = "Location 3",
            city = "Barcelona",
            status = true,
            size = "Small",
            dimension = "2x2x2",
            pricePerHour = 2.0,
        ),
        Locker(
            lockerID = "locker4",
            location = "Location 4",
            city = "Barcelona",
            status = false,
            size = "Large",
            dimension = "4x4x4",
            pricePerHour = 10.0,
        ),
        Locker(
            lockerID = "locker5",
            location = "Location 3",
            city = "Barcelona",
            status = true,
            size = "Small",
            dimension = "2x2x2",
            pricePerHour = 2.0,
        ),
        Locker(
            lockerID = "locker6",
            location = "Location 2",
            city = "Madrid",
            status = false,
            size = "Large",
            dimension = "4x4x4",
            pricePerHour = 10.0,
        )
    ))
//    fun setLockerId(id: String){
//        val filtered = _lockers.value.filter { it.lockerID == id }
//        _lockers.value = filtered
//    }
//    fun setLockerLocation(location: String){
//        val filtered = _lockers.value.filter { it.location == location }
//        _lockers.value = filtered
//    }

    val lockers: StateFlow<List<Locker>> = _lockers

    fun countAvailableLockersByCity(city: String): Int {
        return lockers.value.count { locker ->
            locker.city.equals(city, ignoreCase = true) && locker.status
        }
    }

    fun getLockerById(lockerId: String): Locker? {
        return lockers.value.find { it.lockerID == lockerId }
    }


//    // Get only available lockers
//    val availableLockers: StateFlow<List<Locker>> =
//        _lockers.map { lockers -> lockers.filter { it.status } }
//            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//
//    // Function to reserve a locker
    fun reserveLocker(lockerId: String) {
        _lockers.update { lockers ->
            lockers.map { locker ->
                if (locker.lockerID == lockerId) {
                    locker.copy(status = false) // Mark as reserved
                } else {
                    locker
                }
            }
        }
    }
//
//    // Function to free a locker
//    fun freeLocker(lockerId: String) {
//        _lockers.update { lockers ->
//            lockers.map { locker ->
//                if (locker.lockerID == lockerId) {
//                    locker.copy(status = true) // Mark as available
//                } else {
//                    locker
//                }
//            }
//        }
//    }
}