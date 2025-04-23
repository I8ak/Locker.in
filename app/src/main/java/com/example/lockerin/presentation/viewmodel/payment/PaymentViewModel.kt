package com.example.lockerin.presentation.viewmodel.payment

import androidx.lifecycle.ViewModel
import com.example.lockerin.domain.model.Payment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

class PaymentViewModel: ViewModel(){
    private val _payment = MutableStateFlow<List<Payment>>(listOf(

    ))
    val payments :StateFlow<List<Payment>> = _payment
    fun getPaymentByUserId(userId: String): List<Payment> {
        return payments.value.filter { it.userID == userId }
    }
    fun getPaymentByPaymentId(paymentId: String): Payment? {
        return payments.value.find { it.paymentID == paymentId }
    }
    fun addPayment(payment: Payment) {
        _payment.value = _payment.value + payment
    }
}