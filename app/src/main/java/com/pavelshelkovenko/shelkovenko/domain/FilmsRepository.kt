package com.pavelshelkovenko.shelkovenko.domain

import com.pavelshelkovenko.shelkovenko.domain.models.Film
import com.pavelshelkovenko.shelkovenko.domain.models.FilmDetails


interface FilmsRepository {
    suspend fun getPopularFilms(): Result<List<Film>>

    suspend fun getFilmDetails(filmId: Int): Result<FilmDetails>

    suspend fun searchFilmByKeyword(keyword: String): Result<List<Film>>
}