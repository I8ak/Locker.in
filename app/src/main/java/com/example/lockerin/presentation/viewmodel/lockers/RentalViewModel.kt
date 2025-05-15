package com.example.lockerin.presentation.viewmodel.lockers

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.first
import androidx.lifecycle.viewModelScope
import com.example.lockerin.data.source.remote.CardFirestoreRepository
import com.example.lockerin.data.source.remote.HistoricRentalFirestoreRepository
import com.example.lockerin.data.source.remote.LockerFirestoreRepository
import com.example.lockerin.data.source.remote.PaymentFirestoreRepository
import com.example.lockerin.data.source.remote.RentalFirestoreRepository
import com.example.lockerin.domain.model.HistoricRental
import com.example.lockerin.domain.model.Rental
import com.example.lockerin.domain.model.Tarjeta
import com.example.lockerin.domain.usecase.rental.AddRentalUseCase
import com.example.lockerin.domain.usecase.rental.CountRentalsByUserUseCase
import com.example.lockerin.domain.usecase.rental.DeleteRentalUseCase
import com.example.lockerin.domain.usecase.rental.GetRentalUseCase
import com.example.lockerin.domain.usecase.rental.IsLockerAvailableUseCase
import com.example.lockerin.domain.usecase.rental.ListRentalsByUserIdUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

class RentalViewModel(
    val addRentalUseCase: AddRentalUseCase,
    val listRentalsByUserIdUseCase: ListRentalsByUserIdUseCase,
    val getRentalUseCase: GetRentalUseCase,
    val countLockersUseCase: CountRentalsByUserUseCase,
    val deleteRentalUseCase: DeleteRentalUseCase,
    val lockerRepository: LockerFirestoreRepository,
    val paymentRepository: PaymentFirestoreRepository,
    val cardRepository: CardFirestoreRepository,
    val historicalRentalViewModel: HistoricRentalFirestoreRepository,
    val lockersViewModel: LockersViewModel,
    val rentalFirestoreRepository: RentalFirestoreRepository,
    val isLockerAvailableUseCase: IsLockerAvailableUseCase
) : ViewModel() {
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()
    private val _rentals: MutableStateFlow<Tarjeta?> = MutableStateFlow(null)
    val rentals: StateFlow<List<Rental>> = _userId
        .filterNotNull()
        .flatMapLatest { userId: String ->
            listRentalsByUserIdUseCase(userId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    @SuppressLint("SimpleDateFormat")

    fun setUserId(userId: String) {
        _userId.value = userId
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun addRental(rental: Rental) {
        viewModelScope.launch {
            addRentalUseCase(rental)
        }
    }





    private val _selectedRental = MutableStateFlow<Rental?>(null)
    val selectedRental: StateFlow<Rental?> = _selectedRental.asStateFlow()
    fun getRentalById(rentalId: String) {
        viewModelScope.launch {
            val rental = getRentalUseCase(rentalId)
            _selectedRental.value = rental
        }
    }

    private val _rentalCount = MutableStateFlow(0)
    val rentalCount: StateFlow<Int> = _rentalCount.asStateFlow()

    fun countRentals(userId: String) {
        viewModelScope.launch {
            val count = countLockersUseCase(userId)
            _rentalCount.value = count
        }
    }
    fun deleteRental(rental: Rental) {
        viewModelScope.launch {
            deleteRentalUseCase(rental)
            countRentals(rental.userID)
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun checkAndMoveExpiredRentals(userID: String) {
        viewModelScope.launch (Dispatchers.IO){
            Log.d("FirestoreDebug", ">>> DEBUG AUTH <<< Current authenticated UID before writes: $userID")
            val rentals = rentalFirestoreRepository.getRentalByUserId(userID).first()
            Log.d("FirestoreDebug", "Rentals fetched: ${rentals.size}")


            rentals.forEach { rental ->

                val rentalEndDateTime = rental.endDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()

                Log.d("FirestoreDebug", "Converted to LocalDate: $rentalEndDateTime")
                Log.d("FirestoreDebug", "Rental ID: ${rental.rentalID} ends at: ${rental.endDate}, converted: $rentalEndDateTime, today: ${LocalDate.now()}")


                if (rentalEndDateTime != null && !rentalEndDateTime.isAfter(LocalDateTime.now())) {
                    val locker = lockerRepository.getLockerById(rental.lockerID)
                    val payment = paymentRepository.getPaymentByRentalId(rental.rentalID)
                    val card = cardRepository.getCardById(payment?.cardID ?: "")

                    val historicRentalID = FirebaseFirestore.getInstance().collection("historicRentals").document().id
                    Log.d("FirestoreDebug", "Attempting to update locker: ${locker}")
                    Log.d("FirestoreDebug", "Attempting to update locker: ${payment}")
                    Log.d("FirestoreDebug", "Attempting to update locker: ${card}")
                    Log.d("FirestoreDebug", "Attempting to update locker: ${historicRentalID}")
                    val currentUserUidCheck = FirebaseAuth.getInstance().currentUser?.uid
                    Log.d("FirestoreDebug", ">>> DEBUG AUTH <<< Current authenticated UID before writes: $currentUserUidCheck")
                    Log.d("FirestoreDebug", ">>> DEBUG AUTH <<< rental.userID is: ${rental.userID}")


                    Log.d("FirestoreDebug", "Attempting SAVE historicRental for rentalID: ${rental.rentalID}, with historicUserID: ${rental.userID}")
                    try {
                        historicalRentalViewModel.save(
                            HistoricRental(
                                historicID = historicRentalID,
                                userID = rental.userID,
                                location = locker?.location.orEmpty(),
                                city = locker?.city.orEmpty(),
                                size = locker?.size.orEmpty(),
                                dimension = locker?.dimension.orEmpty(),
                                cardNumber = card?.cardNumber.orEmpty(),
                                typeCard = card?.typeCard.orEmpty(),
                                amount = payment?.amount ?: 0.0,
                                status = true,
                                startDate = rental.startDate,
                                endDate = rental.endDate,
                                createdAt = payment?.createdAt
                            )
                        )
                        Log.d("FirestoreDebug", "Historic rental saved successfully.")
                    }catch (e: Exception) {
                        Log.e("FirestoreDebug", "Error saving historic rental: ${e.message}")
                    }

                    Log.d("FirestoreDebug", "Attempting SET_STATUS on lockerID: ${locker?.lockerID}")
                    lockersViewModel.setStatus(locker?.lockerID.orEmpty(), true)
                    Log.d("FirestoreDebug", "Attempting DELETE rental: ${rental.rentalID}, with rentalUserID: ${rental.userID}")
                    Log.d("FirestoreDebug", "Rental endDate: ${rental.endDate}")



                    deleteRental(rental)
                }
            }
        }
    }

    fun finalizeSpecificRental(rental: Rental) {
        viewModelScope.launch {
            try {
                val locker = lockerRepository.getLockerById(rental.lockerID)
                val payment = paymentRepository.getPaymentByRentalId(rental.rentalID)
                val card = payment?.cardID?.let { cardId ->
                    cardRepository.getCardById(cardId)
                }

                val historicRentalID = FirebaseFirestore.getInstance().collection("historicRentals").document().id

                historicalRentalViewModel.save(
                    HistoricRental(
                        historicID = historicRentalID,
                        userID = rental.userID,
                        location = locker?.location.orEmpty(),
                        city = locker?.city.orEmpty(),
                        size = locker?.size.orEmpty(),
                        dimension = locker?.dimension.orEmpty(),
                        cardNumber = card?.cardNumber.orEmpty(),
                        typeCard = card?.typeCard.orEmpty(),
                        amount = payment?.amount ?: 0.0,
                        status = true,
                        startDate = rental.startDate,
                        endDate = rental.endDate,
                        createdAt = payment?.createdAt
                    )
                )

                lockersViewModel.setStatus(locker?.lockerID.orEmpty(), true)

                rentalFirestoreRepository.deleteRental(rental.rentalID)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private val _lockerAvailability = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val lockerAvailability: StateFlow<Map<String, Boolean>> = _lockerAvailability

    @RequiresApi(Build.VERSION_CODES.O)
    fun isLockerAvailable(
        lockerId: String,
        newStartDate: Date,
        newEndDate: Date
    ) {
        viewModelScope.launch {
            Log.d("RentalViewModel", "Checking availability for locker: $lockerId from $newStartDate to $newEndDate") // Log de inicio

            var isAvailable = isLockerAvailableUseCase(lockerId, newStartDate, newEndDate)

            Log.d("RentalViewModel", "Availability result for locker $lockerId: $isAvailable")
            _lockerAvailability.value = _lockerAvailability.value.toMutableMap().apply {
                put(lockerId, isAvailable)
            }

        }
    }





}