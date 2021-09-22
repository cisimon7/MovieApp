package com.example.movieapp.services.repository.remoteApi

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

val jsonFormatter = Json {
    prettyPrint = true
    isLenient = true
    encodeDefaults = true
    ignoreUnknownKeys = true
}

var client: HttpClient = HttpClient(OkHttp) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(jsonFormatter)
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("Logger Ktor Client =>", message)
            }
        }
        level = LogLevel.INFO
    }

    install(ResponseObserver) {
        onResponse { response ->
            Log.d("HTTP status:", "${response.status.value}")
            Log.d("HTTP status:", "${response.content}")
        }
    }

    defaultRequest {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}