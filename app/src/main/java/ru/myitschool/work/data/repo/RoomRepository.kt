package ru.myitschool.work.data.repo

import ru.myitschool.work.data.dto.BookingRequestDTO
import ru.myitschool.work.data.source.NetworkDataSource
import ru.myitschool.work.domain.book.entities.BookingData
import ru.myitschool.work.domain.room.entities.ScheduleEntity

class RoomRepository(

) {
    suspend fun getRoom(): Result<BookingData.Place>{
        return NetworkDataSource.getRoom().mapCatching { dto->
            BookingData.Place(
                id = dto.id ?: "1",
                name = dto.place.orEmpty()
            )
        }
    }
    suspend fun getSchedule(): Result<List<ScheduleEntity>> {
        return NetworkDataSource.getSchedule().mapCatching { dto ->
            dto?.map { (date, isFree) ->
                ScheduleEntity(
                    date = date,
                    isFree = isFree
                )
            } ?: error("map is null")
        }
    }
    suspend fun createBooking(date: String) : Result<Unit>{
        return NetworkDataSource.createBooking(
            BookingRequestDTO(
                date = date
            )
        )
    }
    suspend fun freeRoom(date: String): Result<Unit>{
        return NetworkDataSource.freeRoom(
            BookingRequestDTO(
                date = date
            )
        )
    }
}