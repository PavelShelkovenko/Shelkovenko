package com.pavelshelkovenko.shelkovenko.presentation.film_details

sealed class FilmDetailsScreenEvent {
    data class GetFilmDetails(
        val filmId: Int
    ): FilmDetailsScreenEvent()
}