package ru.myitschool.work.ui.screen.room

import ru.myitschool.work.domain.book.entities.BookingData

sealed interface RoomDataState{
    data class Content(
        val roomData: BookingData.Place
    ): RoomDataState
    data object Loading : RoomDataState
}