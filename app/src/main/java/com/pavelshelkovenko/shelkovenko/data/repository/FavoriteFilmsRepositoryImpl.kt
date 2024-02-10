package com.pavelshelkovenko.shelkovenko.data.repository

import com.pavelshelkovenko.shelkovenko.data.FilmMapper
import com.pavelshelkovenko.shelkovenko.data.local.FavoriteFilmDao
import com.pavelshelkovenko.shelkovenko.domain.FavoriteFilmsRepository
import com.pavelshelkovenko.shelkovenko.domain.models.Film
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class FavoriteFilmsRepositoryImpl (
    private val favoriteFilmDao: FavoriteFilmDao,
    private val mapper: FilmMapper
): FavoriteFilmsRepository {
    override fun getFavoriteFilmsFlow(): Flow<List<Film>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteFilms(): List<Film> {
        val favoriteFilms = favoriteFilmDao.getFavoriteFilms()
        return favoriteFilms?.map {
            mapper.mapFavouriteFilmEntityToDomain(it)
        } ?: emptyList()
    }

    override suspend fun deleteFavoriteFilm(filmId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun addFavoriteFilm(film: Film) {
        TODO("Not yet implemented")
    }
}