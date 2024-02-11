package com.pavelshelkovenko.shelkovenko.presentation.model

import javax.annotation.concurrent.Immutable

@Immutable
data class FilmUi(
    val id: Int,
    val title: String,
    val year: Int?,
    val posterUrl: String,
    val genre: String,
    val countries: List<String>,
    val isFavorite: Boolean,
)
