package com.example.lockerin.presentation.viewmodel.lockers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.domain.usecase.locker.AddLockerUseCase
import com.example.lockerin.domain.usecase.locker.CountAvalibleLockerByCityUseCase
import com.example.lockerin.domain.usecase.locker.DeleteLockerUseCase
import com.example.lockerin.domain.usecase.locker.EditLockerUseCase
import com.example.lockerin.domain.usecase.locker.GetLockerByIdUseCase
import com.example.lockerin.domain.usecase.locker.ListLockersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class LockersViewModel(
    listLockersUseCase: ListLockersUseCase,
    val addLockerUseCase: AddLockerUseCase,
    val deleteLockerUseCase: DeleteLockerUseCase,
    val getLockerByIdUseCase: GetLockerByIdUseCase,
    val editlockerUseCase: EditLockerUseCase,
    val countAvalibleLockerByCityUseCase: CountAvalibleLockerByCityUseCase
):ViewModel() {
    private val _lockers =listLockersUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
//    fun setLockerId(id: String){
//        val filtered = _lockers.value.filter { it.lockerID == id }
//        _lockers.value = filtered
//    }
//    fun setLockerLocation(location: String){
//        val filtered = _lockers.value.filter { it.location == location }
//        _lockers.value = filtered
//    }

    val lockers: StateFlow<List<Locker>> = _lockers


    private val _availableCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val availableCounts: StateFlow<Map<String, Int>> = _availableCounts.asStateFlow()

    fun countAvailableLockersByCity(city: String) {
        viewModelScope.launch {
            val count = countAvalibleLockerByCityUseCase(city)
            _availableCounts.value = _availableCounts.value.toMutableMap().apply {
                put(city, count)
            }
        }
    }

    suspend fun getLockerById(id: String): Locker? {
        return getLockerByIdUseCase(id)
    }



    fun addLocker(locker: Locker, onIdAlreadyExists: () -> Unit) {
        viewModelScope.launch {
            val existingLocker = getLockerById(locker.lockerID)
            if (existingLocker != null) {
                onIdAlreadyExists()
            } else {
                addLockerUseCase(locker)

            }
        }
    }
    fun deleteLocker(id: Locker) {
        viewModelScope.launch {
            deleteLockerUseCase(id)
        }
    }
    fun editLocker(locker: Locker) {
        viewModelScope.launch {
            editlockerUseCase(locker)
        }
    }


//    // Get only available lockers
//    val availableLockers: StateFlow<List<Locker>> =
//        _lockers.map { lockers -> lockers.filter { it.status } }
//            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//
//    // Function to reserve a locker
//    fun reserveLocker(lockerId: String) {
//        _lockers.update { lockers ->
//            lockers.map { locker ->
//                if (locker.lockerID == lockerId) {
//                    locker.copy(status = false) // Mark as reserved
//                } else {
//                    locker
//                }
//            }
//        }
//    }
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