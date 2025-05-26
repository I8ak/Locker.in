package com.example.lockerin.presentation.viewmodel.rentals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockerin.domain.model.HistoricRental
import com.example.lockerin.domain.usecase.historicRental.CountHistoricUseCase
import com.example.lockerin.domain.usecase.historicRental.EditHistoricRentalUseCase
import com.example.lockerin.domain.usecase.historicRental.ListHistoricRentalUseCase
import com.example.lockerin.domain.usecase.rental.CountRentalsByUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoricalRentalViewModel(
    val listHistoricRentalUseCase: ListHistoricRentalUseCase,
    val editHistoricRentalUseCase: EditHistoricRentalUseCase,
    val countHistoricUseCase: CountHistoricUseCase
) : ViewModel() {
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val historicalRental: StateFlow<List<HistoricRental>> = _userId
        .flatMapLatest { uid ->
            if (uid != null && uid.isNotBlank()) {
                Log.e("HistoricalRentalViewModel", "Getting historic rentals for userId: $uid")
                listHistoricRentalUseCase(uid).catch { e->
                    Log.e("HistoricalRentalViewModel", "Error fetching historical rentals: ${e.message}")
                    emit(emptyList())
                }
            } else {
                Log.w("HistoricalRentalViewModel", "UserId is null or blank, emitting empty list for historical rentals.")
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    fun setUserId(userId: String?) {
        if (userId == null || userId.isBlank()) {
            Log.w("HistoricalRentalViewModel", "setUserId called with null or blank ID. Setting _userId to null.")
            _userId.value = null
            countHistoricRentals(null)
        } else {
            Log.d("HistoricalRentalViewModel", "setUserId called with valid ID: $userId")
            _userId.value = userId
            countHistoricRentals(userId)
        }
    }

    fun setStatus(historicRental: HistoricRental?, status: Boolean) {
        viewModelScope.launch {
            if (historicRental != null) {
                val updatedHistoricRental = historicRental.copy(calificado = status)
                editHistoricRentalUseCase(updatedHistoricRental)
            }
        }
    }

    private val _payedCount = MutableStateFlow(0)
    val payedCount: StateFlow<Int> = _payedCount

    private val _canceledCount = MutableStateFlow(0)
    val canceledCount: StateFlow<Int> = _canceledCount

    fun countHistoricRentals(userId: String?) {
        viewModelScope.launch {
            if (userId != null && userId.isNotBlank()) {
                _payedCount.value = countHistoricUseCase(userId, status = true)
                _canceledCount.value = countHistoricUseCase(userId, status = false)
            } else {
                _payedCount.value = 0
                _canceledCount.value = 0
                Log.w("HistoricalRentalViewModel", "Cannot count historic rentals: userId is null or blank.")
            }
        }
    }




}