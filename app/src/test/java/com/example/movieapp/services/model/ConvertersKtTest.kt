package com.example.movieapp.services.model

import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.test.assertEquals

class ConvertersKtTest {

    @Test
    fun dateConverters() {
        val now = LocalDate(2021, 9, 10)

        assertEquals(now, stringToLocalDate("2021-09-10"))
        assertEquals(localDateToString(now), "2021-09-10")
    }
}