package com.example.lockerin.domain.usecase.payment

import com.example.lockerin.data.source.remote.PaymentFirestoreRepository
import com.example.lockerin.domain.model.Payment
import kotlinx.coroutines.flow.Flow

class GetPaymentUseCase(
    private val paymentRepository: PaymentFirestoreRepository
) {
    suspend operator fun invoke(userId: String): Payment? {
        return paymentRepository.getPaymentById(userId)
    }
}