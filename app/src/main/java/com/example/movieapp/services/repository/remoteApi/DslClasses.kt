@file:Suppress("PropertyName","FunctionName")

package com.example.movieapp.services.repository.remoteApi

import java.util.*


class SortBy() {

    var sort_property: SortProperty = SortProperty.Popularity.Desc

    constructor (sort_parameter: SortBy.() -> Unit) : this() {
        SortBy().apply(sort_parameter)
    }

    val value: String?
        get() =
            sort_property::class.qualifiedName?.split(".")?.takeLast(2)
                ?.joinToString(".")?.lowercase(
                    Locale.UK
                )
}

sealed interface SortProperty {
    sealed interface Popularity : SortProperty {
        object Desc : Popularity
        object Asc : Popularity
    }

    sealed interface ReleaseDate : SortProperty {
        object Desc : ReleaseDate
        object Asc : ReleaseDate
    }

    sealed interface Revenue : SortProperty {
        object Desc : Revenue
        object Asc : Revenue
    }

    sealed interface PrimaryRelease : SortProperty {
        object Desc : PrimaryRelease
        object Asc : PrimaryRelease
    }

    sealed interface OriginalTitle : SortProperty {
        object Desc : OriginalTitle
        object Asc : OriginalTitle
    }

    sealed interface VoteAverage : SortProperty {
        object Desc : VoteAverage
        object Asc : VoteAverage
    }

    sealed interface VoteCount : SortProperty {
        object Desc : VoteCount
        object Asc : VoteCount
    }
}