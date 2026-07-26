package ru.myitschool.work.ui.screen.room

import ru.myitschool.work.domain.book.entities.BookingData
import ru.myitschool.work.domain.room.entities.ScheduleEntity

sealed interface RoomScheduleState{
    data class Error(
        val reason : String?
    ): RoomScheduleState
    data class Content(
        val roomSchedule: List<ScheduleEntity>
    ): RoomScheduleState
    data object Loading : RoomScheduleState
}