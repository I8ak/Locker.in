package com.example.lockerin.presentation.viewmodel.lockers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.domain.usecase.locker.AddLockerUseCase
import com.example.lockerin.domain.usecase.locker.AddPuntuacionUseCase
import com.example.lockerin.domain.usecase.locker.CountAvalibleLockerByCityUseCase
import com.example.lockerin.domain.usecase.locker.DeleteLockerUseCase
import com.example.lockerin.domain.usecase.locker.EditLockerUseCase
import com.example.lockerin.domain.usecase.locker.GetLockerByIdUseCase
import com.example.lockerin.domain.usecase.locker.ListLockersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LockersViewModel(
    listLockersUseCase: ListLockersUseCase,
    val addLockerUseCase: AddLockerUseCase,
    val deleteLockerUseCase: DeleteLockerUseCase,
    val getLockerByIdUseCase: GetLockerByIdUseCase,
    val editLockerUseCase: EditLockerUseCase,
    val countAvalibleLockerByCityUseCase: CountAvalibleLockerByCityUseCase,
    val addPuntuacionUseCase: AddPuntuacionUseCase
):ViewModel() {
    private val _lockers =listLockersUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


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

    private val _selectedLocker = MutableStateFlow<Locker?>(null)
    val selectedLocker: StateFlow<Locker?> = _selectedLocker.asStateFlow()

    fun getLockerById(lockerId: String) {
        viewModelScope.launch {
            val locker = getLockerByIdUseCase(lockerId)
            _selectedLocker.value = locker
        }
    }




    fun addLocker(locker: Locker, onIdAlreadyExists: (id:Boolean) -> Unit) {
        viewModelScope.launch {
            val existingLocker = getLockerByIdUseCase(locker.lockerID)
            if (existingLocker != null) {
                onIdAlreadyExists(true)
            } else {
                addLockerUseCase(locker)
                onIdAlreadyExists(false)

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
            editLockerUseCase(locker)
        }
    }

    fun setStatus(lockerId: String, status: Boolean) {
        viewModelScope.launch {
            val locker = getLockerByIdUseCase(lockerId)
            if (locker != null) {
                val updatedLocker = locker.copy(status = status)
                editLockerUseCase(updatedLocker)
            }
        }
    }

    fun guardarPuntuacionEnFirestore(lockerId: String, nuevaPuntuacion: Float){
        viewModelScope.launch {
            addPuntuacionUseCase(lockerId,nuevaPuntuacion)
        }
    }
    fun clearState() {
        _availableCounts.value = emptyMap()
        _selectedLocker.value = null
    }



}