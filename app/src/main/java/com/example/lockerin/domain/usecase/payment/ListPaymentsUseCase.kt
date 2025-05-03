package com.example.lockerin.domain.usecase.payment

import com.example.lockerin.data.source.remote.PaymentFirestoreRepository
import com.example.lockerin.domain.model.Payment
import kotlinx.coroutines.flow.Flow

class ListPaymentsUseCase(
    private val paymentRepository: PaymentFirestoreRepository
) {
    operator fun invoke(userID: String): Flow<List<Payment>> {
        return paymentRepository.getPaymentByUserId(userID)
    }
}