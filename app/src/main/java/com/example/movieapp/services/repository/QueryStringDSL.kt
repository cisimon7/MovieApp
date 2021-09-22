@file:Suppress("PropertyName", "FunctionName")

package com.example.movieapp.services.repository

import com.example.movieapp.services.repository.remoteApi.SortBy

@DslMarker
annotation class QueryStringMarker

@QueryStringMarker
interface QueryStringElement

data class QueryStringDSL(
    var language: String? = "en-US",
    var region: String? = null,
    var page: Int = 1,
    var include_adult: Boolean? = null,
    var primary_release_year: Int? = null,
    var voteCount: Int? = null,
    var voteAverage: Float? = null,
    var include_video: Boolean = false
) : QueryStringElement {

    var sort_by: String? = SortBy().value
        private set

    constructor (queryPairs: QueryStringDSL.() -> Unit) : this() {
        QueryStringDSL().apply(queryPairs)
    }

    fun sort_by(sort_property: SortBy.() -> Unit) {
        sort_by = SortBy(sort_property).value
    }

    override fun toString(): String {
        val keyValue = "language=$language, region=$region, page=$page, include_adult=$include_adult, primary_release_year=$primary_release_year, vote_count.gte=$voteCount, vote_average.gte=$voteAverage, include_video=$include_video, sort_by=$sort_by"
        return keyValue
            .split(", ")
            .filterNot { it.endsWith("null") }
            .joinToString("&", prefix = "&")
    }
}