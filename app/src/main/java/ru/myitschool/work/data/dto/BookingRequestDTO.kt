package ru.myitschool.work.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingRequestDTO(
    @SerialName("date")
    val date: String
)