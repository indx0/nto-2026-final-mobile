package ru.myitschool.work.data.util

import android.R.attr.value
import android.util.Log
import io.ktor.http.HttpMessageBuilder
import ru.myitschool.work.data.source.LocalDataSource

suspend fun HttpMessageBuilder.addAuth(){
    value.let { headers.append("Authorization", LocalDataSource.getToken().orEmpty())}
}
