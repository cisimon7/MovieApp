package com.example.movieapp.services.model

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
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

    @TypeConverter
    fun dateTimeToString(dateTime: LocalDateTime?): String? {
        return dateTime?.let { with(dateTime) {
            val padStart = monthNumber.toString().padStart(2, '0')
            "$year-$padStart-$dayOfMonth-$hour-$minute-$second"
        } }
    }

    @TypeConverter
    fun stringToDateTime(dateTime: String?): LocalDateTime? {
        return when (dateTime.isNullOrEmpty()) {
            true -> null
            false ->  {
                val dateTimeToList = dateTime.split("-").map { it.toInt() }
                with(dateTimeToList) {
                    LocalDateTime(get(0), get(1), get(2), get(3), get(4), get(5))
                }
            }
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