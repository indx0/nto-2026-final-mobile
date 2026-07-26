package ru.myitschool.work.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RolesDTO(
    @SerialName("roles")
    val roles: List<String>?
)