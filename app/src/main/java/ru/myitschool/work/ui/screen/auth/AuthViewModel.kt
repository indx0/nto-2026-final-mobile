package ru.myitschool.work.ui.screen.auth

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.myitschool.work.R
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.data.source.LocalDataSource
import ru.myitschool.work.data.source.NetworkDataSource
import ru.myitschool.work.domain.auth.CheckAndSaveAuthCodeUseCase
import ru.myitschool.work.domain.auth.CheckCodeFormatUseCase
import ru.myitschool.work.ui.nav.MainScreenDestination

class AuthViewModel : ViewModel() {
    private val checkCodeFormatUseCase by lazy { CheckCodeFormatUseCase() }
    private val checkAndSaveAuthCodeUseCase by lazy { CheckAndSaveAuthCodeUseCase(AuthRepository(
        localDataSource = LocalDataSource,
        networkDataSource = NetworkDataSource
    )) }
    private val _uiState = MutableStateFlow<AuthState>(
        AuthState.Data(
            isEnabledSend = false,
            error = null
        )
    )
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()


    val role: StateFlow<String?> = LocalDataSource.role.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _actionFlow: MutableSharedFlow<AuthAction> = MutableSharedFlow()
    val actionFlow: SharedFlow<AuthAction> = _actionFlow


    private var unAuthAttempts = HashMap<String,Int>()

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Send -> sendData(intent)
            is AuthIntent.TextInput -> {
                updateStateIfData { oldState ->
                    oldState.copy(
                        isEnabledSend = checkCodeFormatUseCase.invoke(intent.login) && intent.password.isNotEmpty(),
                        error = null
                    )
                }
            }
        }
    }
    private fun sendData(intent: AuthIntent.Send){
        viewModelScope.launch {
            if (unAuthAttempts.getOrDefault(intent.login, 0) < 5){
                checkAndSaveAuthCodeUseCase(intent.login, intent.password).fold(
                    onSuccess = {
                        _actionFlow.emit(AuthAction.Open(MainScreenDestination))
                    },
                    onFailure = { error ->
                        updateStateIfData { oldState ->
                            if (error.message.orEmpty().trim() == "401") {
                                unAuthAttempts[intent.login] = unAuthAttempts.getOrDefault(intent.login, 0) + 1
                            }
                            oldState.copy(
                                error = error.message
                            )
                        }
                    }
                )
            }
            else{
                updateStateIfData { oldState ->
                    oldState.copy(
                        error = "Too many attempts"
                    )
                }
            }
        }
    }

    private fun updateStateIfData(lambda: (AuthState.Data) -> AuthState) {
        _uiState.update { state ->
            (state as? AuthState.Data)?.let { lambda.invoke(it) } ?: state
        }

    }
}