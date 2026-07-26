package ru.myitschool.work.ui.screen.main

import kotlinx.collections.immutable.PersistentList

sealed interface RoomState {
    data object Loading: RoomState
    data class Error(
        val error: String
    ): RoomState
    data class Data(
        val place: String
    ): RoomState
}