package ru.myitschool.work.domain.auth

import ru.myitschool.work.data.repo.AuthRepository

class GetTokenUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getToken()
    }
}