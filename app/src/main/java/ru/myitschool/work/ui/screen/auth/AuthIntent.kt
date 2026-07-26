package ru.myitschool.work.ui.screen.auth

sealed interface AuthIntent {
    data class Send(val login: String,
                    val password: String,
                    var unAuthorisedCount : Int = 0): AuthIntent
    data class TextInput(val login: String, val password: String): AuthIntent
}