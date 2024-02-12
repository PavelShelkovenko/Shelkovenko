package com.pavelshelkovenko.shelkovenko.data

import com.google.common.truth.Truth
import com.pavelshelkovenko.shelkovenko.data.local.models.FavoriteFilmEntity
import com.pavelshelkovenko.shelkovenko.data.remote.models.CountryDto
import com.pavelshelkovenko.shelkovenko.data.remote.models.FilmDetailsResponse
import com.pavelshelkovenko.shelkovenko.data.remote.models.FilmDto
import com.pavelshelkovenko.shelkovenko.data.remote.models.FilmsResponse
import com.pavelshelkovenko.shelkovenko.data.remote.models.GenreDto
import com.pavelshelkovenko.shelkovenko.domain.models.Film
import com.pavelshelkovenko.shelkovenko.domain.models.FilmDetails
import org.junit.Before
import org.junit.Test

class FilmMapperTest {
    private lateinit var mapper: FilmMapper

    @Before
    fun setup() {
        mapper = FilmMapper()
    }

    @Test
    fun test_mapDomainToFavoriteFilmEntity_maps_correct() {
        // Arrange
        val expected = FavoriteFilmEntity(
            id = 1,
            title = "1",
            year = 1234,
            posterUrl = "poster",
            genre = "genre1,genre2",
            countries = "country1,country2"
        )
        val domainFilm = Film(
            id = 1,
            title = "1",
            year = 1234,
            posterUrl = "poster",
            genre = listOf("genre1", "genre2"),
            countries = listOf("country1", "country2"),
            isFavorite = true
        )

        // Act
        val actual = mapper.mapDomainToFavoriteFilmEntity(domainFilm)

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun test_mapFavouriteFilmEntityToDomain_maps_correct() {
        // Arrange
        val entity = FavoriteFilmEntity(
            id = 1,
            title = "1",
            year = 1234,
            posterUrl = "poster",
            genre = "genre1,genre2",
            countries = "country1,country2"
        )
        val expected = Film(
            id = 1,
            title = "1",
            year = 1234,
            posterUrl = "poster",
            genre = listOf("genre1", "genre2"),
            countries = listOf("country1", "country2"),
            isFavorite = true
        )

        // Act
        val actual = mapper.mapFavouriteFilmEntityToDomain(entity)

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun test_mapFilmDetailsDtoToDomain_maps_correct() {
        // Arrange
        val response = FilmDetailsResponse(
            id = 1,
            title = "title1",
            posterUrl = "poster1",
            description = "description",
            genres = listOf(GenreDto("genre1")),
            year = 1,
            countries = listOf(CountryDto("country1"), CountryDto("country2"))
        )
        val expected = FilmDetails(
            id = 1,
            title = "title1",
            year = 1,
            posterUrl = "poster1",
            genre = listOf("genre1"),
            countries = listOf("country1", "country2"),
            description = "description",
        )

        // Act
        val actual = mapper.mapFilmDetailsDtoToDomain(response)

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun test_mapListDtoToDomainList_maps_correct() {
        // Arrange
        val filmsResponse = FilmsResponse(
            films = listOf(
                FilmDto(
                    filmId = 1,
                    title = "title1",
                    posterUrl = "poster1",
                    year = 1,
                    genres = listOf(GenreDto("genre1")),
                    countries = listOf(CountryDto("country1"))
                ),
                FilmDto(
                    filmId = 2,
                    title = "title2",
                    posterUrl = "poster2",
                    year = 2,
                    genres = listOf(GenreDto("genre2")),
                    countries = listOf(CountryDto("country2"))
                )
            )
        )
        val expected = listOf(
            Film(
                id = 1,
                title = "title1",
                year = 1,
                posterUrl = "poster1",
                genre = listOf("genre1"),
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

        // Act
        val actual = mapper.mapListDtoToDomainList(filmsResponse.films!!)

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }
}