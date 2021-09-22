package com.example.movieapp

import com.example.movieapp.services.repository.QueryStringDSL
import com.example.movieapp.services.repository.remoteApi.SortProperty
import org.junit.Test
import java.util.*

class QueryStringDSLTest {
    @Test
    fun `print result`() {

        println(
            SortProperty.Popularity.Asc::class.qualifiedName?.split(".")?.takeLast(2)
                ?.joinToString(".")?.lowercase(
                    Locale.UK
                )
        )
    }

    @Test
    fun `class property value pair`() {
        val queryParameters: QueryStringDSL = QueryStringDSL {
            page = 1
            include_adult = true
            sort_by { sort_property = SortProperty.Popularity.Desc }
        }
        val query = queryParameters.toString()
        println(query)
    }
}