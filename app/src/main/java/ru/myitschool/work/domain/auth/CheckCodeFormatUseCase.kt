package ru.myitschool.work.domain.auth

class CheckCodeFormatUseCase {
    operator fun invoke(
        login: String
    ): Boolean {
        return login.length > 4 && login.all { char ->
            char.isLetterOrDigit() &&
                    ((char in 'A'..'Z') || (char in 'a'..'z') || char.isDigit())
        }
    }
}