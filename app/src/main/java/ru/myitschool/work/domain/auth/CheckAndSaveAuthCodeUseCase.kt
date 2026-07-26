package ru.myitschool.work.domain.auth

import ru.myitschool.work.data.dto.RolesDTO
import ru.myitschool.work.data.repo.AuthRepository

class CheckAndSaveAuthCodeUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        login: String,
        password: String
    ): Result<RolesDTO> {
        return repository.checkAndSave(login, password)
    }
}