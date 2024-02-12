package com.pavelshelkovenko.shelkovenko.domain

import com.google.common.truth.Truth
import com.pavelshelkovenko.shelkovenko.domain.models.Film
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
class GetPopularFilmsUseCaseTest {
    private lateinit var getPopularFilmsUseCase: GetPopularFilmsUseCase
    private val filmsRepository: FilmsRepository = mockk()
    private val favoriteFilmsRepository: FavoriteFilmsRepository = mockk()

    @Before
    fun setup() {
        getPopularFilmsUseCase = GetPopularFilmsUseCase(
            filmsRepository = filmsRepository,
            favoriteFilmsRepository = favoriteFilmsRepository
        )
    }

    @Test
    fun test_GetPopularFilmsUseCase_returns_failure_if_filmsRepository_get_films_failure() =
        runTest {
            // Arrange
            val filmsRepositoryResult = Result.failure<List<Film>>(Exception())
            coEvery { filmsRepository.getPopularFilms() } returns filmsRepositoryResult
            val favoriteFilmsResult = listOf(
                Film(
                    id = 1,
                    title = "title1",
                    year = 1,
                    posterUrl = "poster1",
                    genre = listOf("genre1"),
                    countries = listOf("country1"),
                    isFavorite = true
                ),
                Film(
                    id = 2,
                    title = "title2",
                    year = 2,
                    posterUrl = "poster2",
                    genre = listOf("genre2"),
                    countries = listOf("country2"),
                    isFavorite = true
                )
            )
            coEvery { favoriteFilmsRepository.getFavoriteFilms() } returns favoriteFilmsResult

            val expected = filmsRepositoryResult.isFailure

            // Act
            val actual = getPopularFilmsUseCase.invoke()

            // Assert
            Truth.assertThat(actual.isFailure).isEqualTo(expected)
        }

    @Test
    fun test_GetPopularFilmsUseCase_returns_same_result_when_favorite_films_empty() = runTest {
        // Arrange
        val filmsRepositoryResult = listOf(
            Film(
                id = 1,
                title = "title1",
                year = 1,
                posterUrl = "poster1",
                genre = listOf("genre1"),
                countries = listOf("country1"),
                isFavorite = true
            ),
            Film(
                id = 2,
                title = "title2",
                year = 2,
                posterUrl = "poster2",
                genre = listOf("genre2"),
                countries = listOf("country2"),
                isFavorite = true
            )
        )
        coEvery { filmsRepository.getPopularFilms() } returns Result.success(filmsRepositoryResult)
        coEvery { favoriteFilmsRepository.getFavoriteFilms() } returns listOf()

        val expected = Result.success(
            listOf(
                Film(
                    id = 1,
                    title = "title1",
                    year = 1,
                    posterUrl = "poster1",
                    genre = listOf("genre1"),
                    countries = listOf("country1"),
                    isFavorite = true
                ),
                Film(
                    id = 2,
                    title = "title2",
                    year = 2,
                    posterUrl = "poster2",
                    genre = listOf("genre2"),
                    countries = listOf("country2"),
                    isFavorite = true
                )
            )
        )

        // Act
        val actual = getPopularFilmsUseCase.invoke()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun test_GetPopularFilmsUseCase_returns_success_when_favorite_films_exist() = runTest {
        // Arrange
        val filmsRepositoryResult = listOf(
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
            ),
            Film(
                id = 3,
                title = "title3",
                year = 3,
                posterUrl = "poster3",
                genre = listOf("genre3"),
                countries = listOf("country3"),
                isFavorite = false
            )
        )
        val favoriteFilmsRepositoryResult = listOf(
            Film(
                id = 1,
                title = "title1",
                year = 1,
                posterUrl = "poster1",
                genre = listOf("genre1"),
                countries = listOf("country1"),
                isFavorite = true
            ),
            Film(
                id = 3,
                title = "title3",
                year = 3,
                posterUrl = "poster3",
                genre = listOf("genre3"),
                countries = listOf("country3"),
                isFavorite = true
            )
        )
        coEvery { filmsRepository.getPopularFilms() } returns Result.success(filmsRepositoryResult)
        coEvery { favoriteFilmsRepository.getFavoriteFilms() } returns favoriteFilmsRepositoryResult

        val expected = Result.success(
            listOf(
                Film(
                    id = 1,
                    title = "title1",
                    year = 1,
                    posterUrl = "poster1",
                    genre = listOf("genre1"),
                    countries = listOf("country1"),
                    isFavorite = true
                ),
                Film(
                    id = 2,
                    title = "title2",
                    year = 2,
                    posterUrl = "poster2",
                    genre = listOf("genre2"),
                    countries = listOf("country2"),
                    isFavorite = false
                ),
                Film(
                    id = 3,
                    title = "title3",
                    year = 3,
                    posterUrl = "poster3",
                    genre = listOf("genre3"),
                    countries = listOf("country3"),
                    isFavorite = true
                )
            )
        )

        // Act
        val actual = getPopularFilmsUseCase.invoke()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }
}