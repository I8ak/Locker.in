package com.example.lockerin.domain.usecase.payment

import com.example.lockerin.data.source.remote.PaymentFirestoreRepository
import com.example.lockerin.domain.model.Payment

class AddPaymentUseCase(
    private val paymentRepository: PaymentFirestoreRepository
) {
    suspend operator fun invoke(payment: Payment): Unit {
        paymentRepository.save(payment)
    }
}