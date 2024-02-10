package com.pavelshelkovenko.shelkovenko.domain.models

data class FilmDetails(
    val id: Int,
    val title: String,
    val year: Int?,
    val posterUrl: String,
    val genre: List<String>,
    val countries: List<String>,
    val description: String
)
