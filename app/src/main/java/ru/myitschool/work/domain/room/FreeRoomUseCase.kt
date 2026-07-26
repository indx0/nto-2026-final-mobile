package ru.myitschool.work.domain.room

import ru.myitschool.work.data.repo.RoomRepository

class FreeRoomUseCase(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(date: String) = repository.freeRoom(date)
}