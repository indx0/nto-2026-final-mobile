package ru.myitschool.work.ui.screen.auth

sealed interface AuthState {

    data object Loading: AuthState
    data class Data(
        val isEnabledSend: Boolean,
        val error: String?
    ): AuthState
}