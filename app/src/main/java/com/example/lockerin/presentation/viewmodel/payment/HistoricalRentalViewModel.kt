package com.example.lockerin.presentation.viewmodel.payment

import androidx.lifecycle.ViewModel
import com.example.lockerin.domain.model.HistoricRental
import com.example.lockerin.domain.model.Rental
import com.example.lockerin.domain.model.Tarjeta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

class HistoricalRentalViewModel:ViewModel() {
    private val _historicalRental = MutableStateFlow<List<HistoricRental>>(listOf(
        HistoricRental(
            historicID = "1",
            userID = "1",
            rentalID = "1",
            lockerID = "locker1",
            paymentID = "1",
            startDate = Date(),
            endDate = Date()
        ),
    ))
    val historicalRental:StateFlow<List<HistoricRental>> = _historicalRental
    fun getHistoricalRentalByUserId(userId: String): List<HistoricRental> {
        return historicalRental.value.filter { it.userID == userId }
    }
    fun addHistoricalRental(historicalRental: HistoricRental) {
        _historicalRental.value = _historicalRental.value + historicalRental
    }
}