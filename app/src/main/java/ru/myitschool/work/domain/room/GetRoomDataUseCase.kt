package ru.myitschool.work.domain.room

import ru.myitschool.work.data.repo.RoomRepository
import ru.myitschool.work.domain.book.entities.BookingData

class GetRoomDataUseCase(
    private val repository: RoomRepository
) {
    suspend operator fun invoke() : Result<BookingData.Place> = repository.getRoom()
}