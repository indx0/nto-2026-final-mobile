package ru.myitschool.work.data.repo

import android.util.Log
import ru.myitschool.work.data.dto.RolesDTO
import ru.myitschool.work.data.source.LocalDataSource
import ru.myitschool.work.data.source.NetworkDataSource

class AuthRepository(
    private val localDataSource: LocalDataSource = LocalDataSource,
    private val networkDataSource: NetworkDataSource = NetworkDataSource
) {
    suspend fun logout() = localDataSource.logout()
    suspend fun getToken() = localDataSource.getToken()
    suspend fun checkAndSave(login: String, password: String): Result<RolesDTO>{
        localDataSource.logout()
        localDataSource.setToken(login, password)
        Log.d("KTOR", "$login:$password")
        return networkDataSource.checkAuth().onSuccess { dto ->
            localDataSource.setRole(dto.roles ?: emptyList())
        }
    }
}