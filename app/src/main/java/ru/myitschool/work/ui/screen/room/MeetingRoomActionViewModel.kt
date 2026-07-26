package ru.myitschool.work.ui.screen.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.io.files.Path
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.data.repo.RoomRepository
import ru.myitschool.work.data.source.LocalDataSource
import ru.myitschool.work.domain.auth.CheckCodeFormatUseCase
import ru.myitschool.work.domain.auth.LogoutUseCase
import ru.myitschool.work.domain.book.entities.BookingData
import ru.myitschool.work.domain.room.CreateBookingUseCase
import ru.myitschool.work.domain.room.FreeRoomUseCase
import ru.myitschool.work.domain.room.GetRoomDataUseCase
import ru.myitschool.work.domain.room.GetScheduleUseCase
import ru.myitschool.work.domain.room.entities.ScheduleEntity
import ru.myitschool.work.ui.nav.AuthScreenDestination
import ru.myitschool.work.ui.screen.main.MainAction
import ru.myitschool.work.ui.screen.main.MainState

class MeetingRoomActionViewModel() : ViewModel() {
    private val getRoomDataUseCase by lazy { GetRoomDataUseCase(
        repository = RoomRepository()
    ) }
    private val getScheduleUseCase by lazy { GetScheduleUseCase(
        repository = RoomRepository()
    ) }
    private val freeRoomUseCase by lazy { FreeRoomUseCase(
        repository = RoomRepository()
    ) }
    private val createBookingUseCase by lazy { CreateBookingUseCase(
        repository = RoomRepository()
    ) }
    private val logoutUseCase by lazy {
        LogoutUseCase(
            repository = AuthRepository(
                LocalDataSource
            )
        )
    }
    private val _actionFlow: MutableSharedFlow<MeetingRoomAction> = MutableSharedFlow()
    val actionFlow: SharedFlow<MeetingRoomAction> = _actionFlow

    private val _roomDataState = MutableStateFlow<RoomDataState>(RoomDataState.Loading)
    val roomDataState: StateFlow<RoomDataState> = _roomDataState.asStateFlow()

    private val _roomScheduleState = MutableStateFlow<RoomScheduleState>(RoomScheduleState.Loading)
    val roomScheduleState: StateFlow<RoomScheduleState> = _roomScheduleState.asStateFlow()

    private fun fetchData(){
        getRoomData()
        getScheduleData()
    }
    private fun autoUpdateData(){
        viewModelScope.launch{
            while(true){
                getScheduleData()
                delay(60000)
            }
        }

    }
    init{
        autoUpdateData()
    }
    private fun getRoomData(){
        viewModelScope.launch {
            _roomDataState.emit(RoomDataState.Loading)
            getRoomDataUseCase().fold(
                onSuccess = { dto ->
                    _roomDataState.emit(RoomDataState.Content(
                        BookingData.Place(
                            id = dto.id,
                            name = dto.name
                        )
                    ))
                },
                onFailure = { e ->
                    _roomScheduleState.emit(RoomScheduleState.Error(e.message.orEmpty()))
                }
            )
        }
    }
    private fun getScheduleData(){
        viewModelScope.launch {
            _roomScheduleState.emit(RoomScheduleState.Loading)
            getScheduleUseCase().fold(
                onSuccess = { dto ->
                    _roomScheduleState.emit(RoomScheduleState.Content(dto))
                },
                onFailure = { e ->
                    _roomScheduleState.emit(RoomScheduleState.Error(e.message.orEmpty()))
                }
            )
        }
    }
    private fun freeRoom(date: String){
        viewModelScope.launch {
            _roomScheduleState.emit(RoomScheduleState.Error(null))
            freeRoomUseCase(date).fold(
                onSuccess = {
                    getScheduleData()
                },
                onFailure = { e ->
                    _roomScheduleState.emit(RoomScheduleState.Error(e.message.orEmpty()))
                }
            )
        }
    }
    private fun createBooking(date: String){
        viewModelScope.launch {
            _roomScheduleState.emit(RoomScheduleState.Error(null))
            createBookingUseCase(date).fold(
                onSuccess = {
                    getScheduleData()
                },
                onFailure = { e ->
                    _roomScheduleState.emit(RoomScheduleState.Error(e.message.orEmpty()))
                }
            )
        }

    }
    private fun logout(){
        viewModelScope.launch {
            logoutUseCase.invoke()
            _actionFlow.emit(MeetingRoomAction.Open(AuthScreenDestination, true))
        }
    }


    fun onIntent(intent: MeetingRoomIntent){
        when(intent){
            is MeetingRoomIntent.GetData -> fetchData()
            is MeetingRoomIntent.CreateBooking -> createBooking(intent.date)
            is MeetingRoomIntent.FreeRoom -> freeRoom(intent.date)
            is MeetingRoomIntent.Logout -> logout()
        }
    }
}