package com.pavelshelkovenko.shelkovenko.presentation.model

import javax.annotation.concurrent.Immutable

@Immutable
data class FilmDetailsUi(
    val id: Int,
    val posterUrl: String,
    val title: String,
    val description: String,
    val genres: String,
    val countries: String
)
