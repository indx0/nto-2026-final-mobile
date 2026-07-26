package ru.myitschool.work.data.source

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ru.myitschool.work.App
import kotlin.io.encoding.Base64

object LocalDataSource {
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = STORE)
    private const val STORE = "AUTH-STORE"
    private const val TOKEN_KEY = "TOKEN"
    private const val ROLE_KEY = "ROLE"

    private var tokenCache: String? = null
    private var roleCache: String? = null

    val role : Flow<String?> = App.context.userDataStore.data.map{ prefs ->
        prefs[stringPreferencesKey(ROLE_KEY)]
    }


    suspend fun setToken(login: String, password: String) {
        val tokenPhrase = "$login:$password"
        App.context.userDataStore.edit { preferences ->
            val prefKey = stringPreferencesKey(TOKEN_KEY)
            preferences[prefKey] = "Basic ${Base64.encode(tokenPhrase.toByteArray())}"
        }
    }
    suspend fun getToken(): String? {
        if (tokenCache == null) {
            tokenCache = App.context.userDataStore.data
                .firstOrNull()
                ?.let { preferences ->
                    preferences[stringPreferencesKey(TOKEN_KEY)]
                }
        }
        return tokenCache
    }
    suspend fun setRole(roles: List<String>) {
        App.context.userDataStore.edit { preferences ->
            val prefKey = stringPreferencesKey(ROLE_KEY)
            preferences[prefKey] = if (roles.isNotEmpty()) roles[0] else "employee"
            Log.d("KTOR", "СОХРАНЕНО ${roles[0]}")
        }
    }
    suspend fun logout() {
        tokenCache = null
        roleCache = null
        App.context.userDataStore.edit { preferences ->
            val tokenKey = stringPreferencesKey(TOKEN_KEY)
            val roleKey = stringPreferencesKey(ROLE_KEY)
            preferences.remove(tokenKey)
            preferences.remove(roleKey)
        }
    }


}