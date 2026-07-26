package ru.myitschool.work.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.data.source.LocalDataSource
import ru.myitschool.work.domain.auth.LogoutUseCase
import ru.myitschool.work.domain.main.GetMainDataUseCase
import ru.myitschool.work.ui.nav.AuthScreenDestination
import ru.myitschool.work.ui.nav.BookScreenDestination
import ru.myitschool.work.ui.screen.room.MeetingRoomAction
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.getValue

class MainViewModel : ViewModel() {
    private val getMainDataUseCase by lazy { GetMainDataUseCase(BookRepository()) }
    private val logoutUseCase by lazy {
        LogoutUseCase(
            repository = AuthRepository(
                LocalDataSource
            )
        )
    }
    private val _uiState = MutableStateFlow<MainState>(MainState.Loading)
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()
    private val _actionFlow: MutableSharedFlow<MainAction> = MutableSharedFlow()
    val actionFlow: SharedFlow<MainAction> = _actionFlow

    init {
        refresh()
    }
    private fun logout(){
        viewModelScope.launch {
            logoutUseCase()
            _actionFlow.emit(MainAction.Open(AuthScreenDestination, true))
        }
    }
    private fun addBooking(){
        viewModelScope.launch {
            _actionFlow.emit(MainAction.Open(BookScreenDestination))
        }
    }
    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { MainState.Loading }
            _uiState.update {
                getMainDataUseCase().fold(
                    onSuccess = { data ->
                        MainState.Data(
                            name = data.name,
                            photoUrl = data.photoUrl,
                            books = data.book.map { book ->
                                MainState.Data.Book(
                                    date = LocalDate
                                        .parse(book.date)
                                        .format(
                                            DateTimeFormatter.ofPattern(DATE_FORMAT)
                                        ),
                                    place = book.place
                                )
                            }.toPersistentList()
                        )
                    },
                    onFailure = { error ->
                        MainState.Error(
                            error = error.message?.takeIf { it.isNotBlank() } ?: "Unknown error"
                        )
                    }
                )
            }
        }
    }
    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.Add -> addBooking()
            is MainIntent.Refresh -> refresh()
            is MainIntent.Logout -> logout()
        }
    }
    private companion object {
        const val DATE_FORMAT = "dd.MM.yyyy"
    }
}