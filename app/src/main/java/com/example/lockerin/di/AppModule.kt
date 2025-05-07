package com.example.lockerin.di

import com.example.lockerin.data.source.remote.CardFirestoreRepository
import com.example.lockerin.data.source.remote.HistoricRentalFirestoreRepository
import com.example.lockerin.data.source.remote.LockerFirestoreRepository
import com.example.lockerin.data.source.remote.PaymentFirestoreRepository
import com.example.lockerin.data.source.remote.RentalFirestoreRepository
import com.example.lockerin.data.source.remote.UserFirestoreRepository
import com.example.lockerin.domain.usecase.card.AddCardUseCase
import com.example.lockerin.domain.usecase.card.DeleteCardUseCase
import com.example.lockerin.domain.usecase.card.GetCardByIdUseCase
import com.example.lockerin.domain.usecase.card.GetCardByUserIdUseCase
import com.example.lockerin.domain.usecase.card.ListCardUseCase
import com.example.lockerin.domain.usecase.historicRental.AddHistoricRentalUseCase
import com.example.lockerin.domain.usecase.historicRental.ListHistoricRentalUseCase
import com.example.lockerin.domain.usecase.locker.AddLockerUseCase
import com.example.lockerin.domain.usecase.locker.CountAvalibleLockerByCityUseCase
import com.example.lockerin.domain.usecase.locker.DeleteLockerUseCase
import com.example.lockerin.domain.usecase.locker.EditLockerUseCase
import com.example.lockerin.domain.usecase.locker.GetLockerByIdUseCase
import com.example.lockerin.domain.usecase.locker.ListLockersUseCase
import com.example.lockerin.domain.usecase.payment.AddPaymentUseCase
import com.example.lockerin.domain.usecase.payment.GetPaymentUseCase
import com.example.lockerin.domain.usecase.payment.ListPaymentsUseCase
import com.example.lockerin.domain.usecase.rental.AddRentalUseCase
import com.example.lockerin.domain.usecase.rental.CountRentalsByUserUseCase
import com.example.lockerin.domain.usecase.rental.DeleteRentalUseCase
import com.example.lockerin.domain.usecase.rental.GetRentalUseCase
import com.example.lockerin.domain.usecase.rental.ListRentalsByUserIdUseCase
import com.example.lockerin.domain.usecase.user.DeleteUserUseCase
import com.example.lockerin.domain.usecase.user.GetUserUseCase
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.lockers.RentalViewModel
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.payment.HistoricalRentalViewModel
import com.example.lockerin.presentation.viewmodel.payment.PaymentViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module  {
    single { FirebaseFirestore.getInstance() }
    //Repositories
    single { UserFirestoreRepository(get()) }
    single { LockerFirestoreRepository(get()) }
    single { CardFirestoreRepository(get()) }
    single { RentalFirestoreRepository(get()) }
    single { PaymentFirestoreRepository(get()) }
    single { HistoricRentalFirestoreRepository(get()) }

    //Users
    factory { GetUserUseCase(get()) }
    factory { DeleteUserUseCase(get()) }

    //Lockers
    factory { ListLockersUseCase(get()) }
    factory { AddLockerUseCase(get()) }
    factory { DeleteLockerUseCase(get())  }
    factory { GetLockerByIdUseCase(get()) }
    factory { EditLockerUseCase(get()) }
    factory { CountAvalibleLockerByCityUseCase(get()) }

    //Cards
    factory { ListCardUseCase(get()) }
    factory { AddCardUseCase(get()) }
    factory { DeleteCardUseCase(get()) }
    factory { GetCardByIdUseCase(get()) }
    factory { GetCardByUserIdUseCase(get()) }

    //Rentals
    factory { AddRentalUseCase(get()) }
    factory { ListRentalsByUserIdUseCase(get()) }
    factory { GetRentalUseCase(get()) }
    factory { CountRentalsByUserUseCase(get()) }
    factory { DeleteRentalUseCase(get()) }

    //Payments
    factory { AddPaymentUseCase(get()) }
    factory { ListPaymentsUseCase(get()) }
    factory { GetPaymentUseCase(get()) }

    //HisoricalRentals
    factory { ListHistoricRentalUseCase(get()) }
    factory { AddHistoricRentalUseCase(get()) }


    //ViewModels
    viewModel { UsersViewModel(get(),get()) }
    viewModel { AuthViewModel() }
    viewModel { LockersViewModel(get(),get(),get(),get(),get(),get()) }
    viewModel { CardsViewModel(get(),get(),get(),get(),get()) }
    viewModel { RentalViewModel(get(),get(),get(),get(),get()) }
    viewModel { PaymentViewModel(get(),get(),get()) }
    viewModel { HistoricalRentalViewModel(get(),get()) }


}