package com.example.lockerin.presentation.viewmodel.payment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.domain.model.Payment
import com.example.lockerin.domain.model.Rental
import com.example.lockerin.domain.usecase.payment.AddPaymentUseCase
import com.example.lockerin.domain.usecase.payment.GetPaymentUseCase
import com.example.lockerin.domain.usecase.payment.ListPaymentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class PaymentViewModel(
    val addPaymentUseCase: AddPaymentUseCase,
    val listPaymentsUseCase: ListPaymentsUseCase,
    val getPaymentUseCase: GetPaymentUseCase
): ViewModel(){
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()
    private val _payments: MutableStateFlow<Payment?> = MutableStateFlow(null)
    val payments: StateFlow<List<Payment>> = _userId
        .filterNotNull()
        .flatMapLatest { userId: String ->
            listPaymentsUseCase(userId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun getPaymentByUserId(userId: String): Payment? {
        return payments.value.find { it.userID == userId }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addPayment(payment: Payment) {
        viewModelScope.launch {
            addPaymentUseCase(payment)
        }
    }

    private val _selectedPayment = MutableStateFlow<Payment?>(null)
    val selectedPayment: StateFlow<Payment?> = _selectedPayment.asStateFlow()

    fun getPaymentByPaymentId(paymentId: String) {
        viewModelScope.launch {
            val payment = getPaymentUseCase(paymentId)
            _selectedPayment.value = payment
        }
    }

    fun setUserId(userId: String) {
        _userId.value = userId
    }
}


