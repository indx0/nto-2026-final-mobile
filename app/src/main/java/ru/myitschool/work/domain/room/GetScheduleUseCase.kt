package ru.myitschool.work.domain.room

import ru.myitschool.work.data.repo.RoomRepository
import ru.myitschool.work.domain.room.entities.ScheduleEntity

class GetScheduleUseCase(
    private val repository: RoomRepository
) {
    suspend operator fun invoke() : Result<List<ScheduleEntity>> = repository.getSchedule()
}