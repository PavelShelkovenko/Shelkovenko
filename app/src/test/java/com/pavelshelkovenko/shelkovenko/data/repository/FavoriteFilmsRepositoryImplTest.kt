package com.pavelshelkovenko.shelkovenko.data.repository

import com.google.common.truth.Truth
import com.pavelshelkovenko.shelkovenko.data.FilmMapper
import com.pavelshelkovenko.shelkovenko.data.local.FavoriteFilmDao
import com.pavelshelkovenko.shelkovenko.data.local.models.FavoriteFilmEntity
import com.pavelshelkovenko.shelkovenko.domain.FavoriteFilmsRepository
import com.pavelshelkovenko.shelkovenko.domain.models.Film
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FavoriteFilmsRepositoryImplTest {

    private lateinit var repository: FavoriteFilmsRepository

    private val favoriteFilmDao: FavoriteFilmDao = mockk()
    private val mapper: FilmMapper = mockk()

    @Before
    fun setup() {
        repository = FavoriteFilmsRepositoryImpl(
            favoriteFilmDao = favoriteFilmDao,
            mapper = mapper
        )
    }

    @Test
    fun test_getFavoriteFilms_returns_films_from_db() = runTest {
        // Arrange
        val filmsFromDb = listOf(
            FavoriteFilmEntity(
                id = 1,
                title = "title1",
                posterUrl = "poster1",
                year = 1,
                genre = "genre1,genre2",
                countries = "country1"
            ),
            FavoriteFilmEntity(
                id = 2,
                title = "title2",
                posterUrl = "poster2",
                year = 2,
                genre = "genre2",
                countries = "country2"
            )
        )
        val mappedFilms = listOf(
            Film(
                id = 1,
                title = "title1",
                year = 1,
                posterUrl = "poster1",
                genre = listOf("genre1", "genre2"),
                countries = listOf("country1"),
                isFavorite = false
            ),
            Film(
                id = 2,
                title = "title2",
                year = 2,
                posterUrl = "poster2",
                genre = listOf("genre2"),
                countries = listOf("country2"),
                isFavorite = false
            )
        )
        coEvery { favoriteFilmDao.getFavoriteFilms() } returns filmsFromDb
        every { mapper.mapFavouriteFilmEntityToDomain(filmsFromDb[0]) } returns mappedFilms[0]
        every { mapper.mapFavouriteFilmEntityToDomain(filmsFromDb[1]) } returns mappedFilms[1]

        // Act
        val actual = repository.getFavoriteFilms()

        // Assert
        Truth.assertThat(actual).isEqualTo(mappedFilms)
    }

    @Test
    fun test_getFavoriteFilms_returns_emptyList_when_db_empty() = runTest {
        // Arrange
        coEvery { favoriteFilmDao.getFavoriteFilms() } returns null

        // Act
        val actual = repository.getFavoriteFilms()

        // Assert
        Truth.assertThat(actual).isEqualTo(emptyList<Film>())
    }

    @Test
    fun test_searchFavoriteFilms_returns_films_from_db() = runTest {
        // Arrange
        val query = "query"
        val filmsFromDb = listOf(
            FavoriteFilmEntity(
                id = 1,
                title = "title1",
                posterUrl = "poster1",
                year = 1,
                genre = "genre1,genre2",
                countries = "country1"
            ),
            FavoriteFilmEntity(
                id = 2,
                title = "title2",
                posterUrl = "poster2",
                year = 2,
                genre = "genre2",
                countries = "country2"
            )
        )
        val mappedFilms = listOf(
            Film(
                id = 1,
                title = "title1",
                year = 1,
                posterUrl = "poster1",
                genre = listOf("genre1", "genre2"),
                countries = listOf("country1"),
                isFavorite = false
            ),
            Film(
                id = 2,
                title = "title2",
                year = 2,
                posterUrl = "poster2",
                genre = listOf("genre2"),
                countries = listOf("country2"),
                isFavorite = false
            )
        )
        coEvery { favoriteFilmDao.searchFavoriteFilms(query) } returns filmsFromDb
        every { mapper.mapFavouriteFilmEntityToDomain(filmsFromDb[0]) } returns mappedFilms[0]
        every { mapper.mapFavouriteFilmEntityToDomain(filmsFromDb[1]) } returns mappedFilms[1]

        // Act
        val actual = repository.searchFavoriteFilms(query)

        // Assert
        Truth.assertThat(actual).isEqualTo(mappedFilms)
    }
    @Test
    fun test_searchFavoriteFilms_returns_emptyList_when_db_empty() = runTest {
        // Arrange
        val query = "query"
        coEvery { favoriteFilmDao.searchFavoriteFilms(query) } returns null

        // Act
        val actual = repository.searchFavoriteFilms(query)

        // Assert
        Truth.assertThat(actual).isEqualTo(emptyList<Film>())
    }
}