package com.example.movieapp.services.model

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json {
        prettyPrint = true
        isLenient = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun statusToString(status: Status?): String {
        return status?.name ?: ""
    }

    @TypeConverter
    fun stringToStatus(status: String?): Status {
        return when (status.isNullOrEmpty()) {
            true -> Status.Unknown
            false -> Status.valueOf(status)
        }
    }

    @TypeConverter
    fun dateToString(date: LocalDate?): String? {
        return date?.let { localDateToString(date) }
    }

    @TypeConverter
    fun stringToDate(date: String?): LocalDate? {
        return when (date.isNullOrEmpty()) {
            true -> null
            false -> stringToLocalDate(date)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @TypeConverter
    fun listToStrings(list: List<String>): String {
        return json.encodeToString(ListSerializer(String.serializer()), list)
    }

    @TypeConverter
    fun stringToList(value: String): List<String> {
        return json.decodeFromString(ListSerializer(String.serializer()), value)
    }
}

fun stringToLocalDate(date: String): LocalDate {
    val (year, month, day) = date.split("-").map { it.toInt() }
    return LocalDate(year, month, day)
}

fun localDateToString(date: LocalDate): String {
    return with(date) {
        val padStart = monthNumber.toString().padStart(2, '0')
        "$year-$padStart-$dayOfMonth"
    }
}