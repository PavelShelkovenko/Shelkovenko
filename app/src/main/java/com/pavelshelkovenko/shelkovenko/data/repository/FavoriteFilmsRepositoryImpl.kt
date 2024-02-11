package com.pavelshelkovenko.shelkovenko.data.repository

import com.pavelshelkovenko.shelkovenko.data.FilmMapper
import com.pavelshelkovenko.shelkovenko.data.local.FavoriteFilmDao
import com.pavelshelkovenko.shelkovenko.data.local.models.FavoriteFilmEntity
import com.pavelshelkovenko.shelkovenko.domain.FavoriteFilmsRepository
import com.pavelshelkovenko.shelkovenko.domain.models.Film
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class FavoriteFilmsRepositoryImpl (
    private val favoriteFilmDao: FavoriteFilmDao,
    private val mapper: FilmMapper
): FavoriteFilmsRepository {
    override fun getFavoriteFilmsFlow(): Flow<List<Film>> {
        return favoriteFilmDao.getFavoriteFilmsFlow().map { listFavoriteFilmEntity ->
            listFavoriteFilmEntity.map {
                mapper.mapFavouriteFilmEntityToDomain(it)
            }
        }
    }

    override suspend fun getFavoriteFilms(): List<Film> {
        val favoriteFilms = favoriteFilmDao.getFavoriteFilms()
        return favoriteFilms?.map {
            mapper.mapFavouriteFilmEntityToDomain(it)
        } ?: emptyList()
    }

    override suspend fun deleteFavoriteFilm(filmId: Int) {
        favoriteFilmDao.deleteFavoriteFilm(filmId = filmId)
    }

    override suspend fun addFavoriteFilm(film: Film) {
        val favoriteFilmEntity = mapper.mapDomainToFavoriteFilmEntity(film = film)
        favoriteFilmDao.putFavoriteFilm(favoriteFilmEntity)
    }
}