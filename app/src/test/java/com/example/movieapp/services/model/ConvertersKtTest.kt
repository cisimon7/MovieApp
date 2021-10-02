package com.example.movieapp.services.model

import com.example.movieapp.ui.fragmentReminderList.timeLeftFormatter
import junit.framework.Assert.assertEquals
import kotlinx.datetime.*
import org.junit.Test
import kotlin.random.Random


class ConvertersKtTest {

    private val converters = Converters()

    @Test
    fun dateConverters() {
        val now = LocalDate(2021, 9, 10)

        assertEquals(now, stringToLocalDate("2021-09-10"))
        assertEquals(localDateToString(now), "2021-09-10")
    }

    @Test
    fun listStringToString() {
        val genres = listOf("Action", "Science Fiction", "Thriller")
        val stringList = converters.listToStrings(genres)
        val listString = converters.stringToList(stringList)
        println(stringList)
        println(listString.toString())
        assertEquals(genres, listString)
    }

    @Test
    fun localDateTimeTest() {
        val tz = TimeZone.currentSystemDefault()
        val now = Clock.System.now()
        val dateTimeFromString = converters.dateTimeToString(now.toLocalDateTime(tz))

        println(now)
        println(dateTimeFromString)

        /*assertEquals(now, converters.stringToDateTime(dateTimeFromString))*/
    }

    @Test
    fun randomDate() {

        val tz = TimeZone.currentSystemDefault()
        val rndDate = Instant.fromEpochMilliseconds(
            Random.nextLong(
                LocalDateTime(2021, 9, 27, 0, 0, 0, 0)
                    .toInstant(tz).toEpochMilliseconds(),
                LocalDateTime(2021, 10, 30, 0, 0, 0, 0)
                    .toInstant(tz).toEpochMilliseconds()
            )
        ).toLocalDateTime(tz)

        val now = Clock.System.now()

        println(timeLeftFormatter(now - rndDate.toInstant(tz)))
    }
}