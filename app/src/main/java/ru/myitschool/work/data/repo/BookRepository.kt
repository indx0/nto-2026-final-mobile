package ru.myitschool.work.data.repo

import ru.myitschool.work.data.dto.BookRequestDto
import ru.myitschool.work.data.source.NetworkDataSource
import ru.myitschool.work.domain.book.entities.BookRequestData
import ru.myitschool.work.domain.book.entities.BookingData
import ru.myitschool.work.domain.main.entities.MainInfoEntity

class BookRepository() {
    suspend fun getInfo(): Result<MainInfoEntity> {
        return NetworkDataSource.getInfo().mapCatching { dto ->
            MainInfoEntity(
                name = dto.name ?: error("Name is null"),
                photoUrl = dto.photoUrl ?: error("Photo url is null"),
                book = dto.booking?.mapNotNull { (date, place) ->
                    MainInfoEntity.Book(
                        date = date,
                        place = place.place ?: return@mapNotNull null
                    )
                } ?: listOf(),
            )
        }
    }

    suspend fun getBookingInfo(): Result<List<BookingData>> {
        return NetworkDataSource.getBooking().mapCatching { dto ->
            dto?.map { (date, places) ->
                BookingData(
                    date = date,
                    places = places.mapNotNull { place ->
                        BookingData.Place(
                            id = place.id ?: return@mapNotNull null,
                            name = place.place ?: return@mapNotNull null
                        )
                    }
                )
            } ?: error("map is null")
        }
    }

    suspend fun sendBook(data: BookRequestData): Result<Unit> {
        val dto = BookRequestDto(data.date, data.placeId)
        return NetworkDataSource.addBook(dto)
    }
}