package ru.myitschool.work.data.source

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.myitschool.work.core.Constants
import ru.myitschool.work.data.Network.client
import ru.myitschool.work.data.dto.PlaceDto
import ru.myitschool.work.data.dto.BookRequestDto
import ru.myitschool.work.data.dto.BookingRequestDTO
import ru.myitschool.work.data.dto.RolesDTO
import ru.myitschool.work.data.dto.UserDto
import ru.myitschool.work.data.util.addAuth

object NetworkDataSource {

    suspend fun checkAuth(): Result<RolesDTO> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.post("${Constants.HOST}/api${Constants.LOGIN_URL}"){
                addAuth()
            }
            when (result.status) {
                HttpStatusCode.OK -> result.body()
                HttpStatusCode.Unauthorized -> error(result.status)
                else -> error("Error: ${result.status}")
            }
        }
    }

    suspend fun getInfo(): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.get("${Constants.HOST}/api${Constants.INFO_URL}"){
                addAuth()
            }
            when(result.status){
                HttpStatusCode.OK -> result.body()
                else -> error("Error: ${result.status}")
            }
        }
    }

    suspend fun getBooking(): Result<Map<String, List<PlaceDto>>?> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.get("${Constants.HOST}/api${Constants.BOOKING_URL}"){
                addAuth()
            }
            when (result.status) {
                HttpStatusCode.OK -> result.body<Map<String, List<PlaceDto>>>()
                else -> error("Error: ${result.status}")
            }
        }
    }

    suspend fun addBook(data: BookRequestDto): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.post("${Constants.HOST}/api${Constants.BOOK_URL}") {

                addAuth()
                setBody(data)
            }
            when (result.status) {
                HttpStatusCode.Created -> Unit
                else -> error("Error: ${result.status}")
            }
        }
    }
    suspend fun getRoom(): Result<PlaceDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.get("${Constants.HOST}/api${Constants.PLACE_URL}") {
                addAuth()
            }
            when (result.status) {
                HttpStatusCode.OK -> result.body()
                else -> error("Error: ${result.status}")
            }
        }
    }
    suspend fun getSchedule(): Result<Map<String, Boolean>?> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.get("${Constants.HOST}/api${Constants.SCHEDULE_URL}") {
                addAuth()
            }
            when (result.status) {
                HttpStatusCode.OK -> result.body<Map<String, Boolean>>()
                else -> error("Error: ${result.status}")
            }
        }
    }
    suspend fun createBooking(dto: BookingRequestDTO): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.post("${Constants.HOST}/api${Constants.PLACE_BOOK_URL}") {
                setBody(dto)
                addAuth()
            }
            when (result.status) {
                HttpStatusCode.Created -> Unit
                else -> error("Error: ${result.status}")
            }
        }
    }
    suspend fun freeRoom(dto: BookingRequestDTO): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.delete("${Constants.HOST}/api${Constants.PLACE_URL}") {
                setBody(dto)
                addAuth()
            }
            when (result.status) {
                HttpStatusCode.OK -> Unit
                else -> error("Error: ${result.status}")
            }
        }
    }
}