package com.example.lockerin.presentation.viewmodel.rentals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockerin.domain.model.HistoricRental
import com.example.lockerin.domain.usecase.historicRental.AddHistoricRentalUseCase
import com.example.lockerin.domain.usecase.historicRental.EditHistoricRentalUseCase
import com.example.lockerin.domain.usecase.historicRental.ListHistoricRentalUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoricalRentalViewModel(
    val listHistoricRentalUseCase: ListHistoricRentalUseCase,
    val editHistoricRentalUseCase: EditHistoricRentalUseCase
) : ViewModel() {
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()
    private val _historicalRental: MutableStateFlow<HistoricRental?> = MutableStateFlow(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val historicalRental: StateFlow<List<HistoricRental>> = _userId
        .filterNotNull()
        .flatMapLatest { userId: String ->
            Log.e("HistoricalRentalViewModel", "Getting historic rentals for userId: ${userId}")
            listHistoricRentalUseCase(userId).catch { e->
                Log.e("HistoricalRentalViewModel", "Error fetching historical rentals: ${e.message}")
                emit(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    fun setUserId(userId: String) {
        _userId.value = userId
    }

    fun setStatus(historicRental: HistoricRental, status: Boolean) {
        viewModelScope.launch {

            if (historicRental != null) {
                val updatedHistoricRental = historicRental.copy(calificado = status)
                editHistoricRentalUseCase(updatedHistoricRental)
            }
        }
    }



}