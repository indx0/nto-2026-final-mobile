package ru.myitschool.work.ui.screen.room

import ru.myitschool.work.ui.nav.AppDestination
import ru.myitschool.work.ui.screen.book.BookIntent

sealed interface MeetingRoomAction {
    class Open(
        val destination: AppDestination,
        val clearBackStack: Boolean = false
    ): MeetingRoomAction
}