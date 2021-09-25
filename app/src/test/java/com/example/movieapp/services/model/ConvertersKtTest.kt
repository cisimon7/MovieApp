package com.example.movieapp.services.model

import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.test.assertEquals

class ConvertersKtTest {

    val converters = Converters()

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
}