package ru.myitschool.work.ui.screen.room

sealed interface MeetingRoomIntent {
    data object GetData: MeetingRoomIntent
    data object Logout : MeetingRoomIntent
    data class FreeRoom(
        val date: String
    ): MeetingRoomIntent
    data class CreateBooking(
        val date: String
    ): MeetingRoomIntent

}