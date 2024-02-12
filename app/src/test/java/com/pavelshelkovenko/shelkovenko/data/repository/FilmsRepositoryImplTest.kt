package com.pavelshelkovenko.shelkovenko.data.repository


import com.google.common.truth.Truth
import com.google.gson.Gson
import com.pavelshelkovenko.shelkovenko.data.FilmMapper
import com.pavelshelkovenko.shelkovenko.data.local.CacheDao
import com.pavelshelkovenko.shelkovenko.data.local.models.CacheEntity
import com.pavelshelkovenko.shelkovenko.data.remote.ApiService
import com.pavelshelkovenko.shelkovenko.data.remote.models.CountryDto
import com.pavelshelkovenko.shelkovenko.data.remote.models.FilmDetailsResponse
import com.pavelshelkovenko.shelkovenko.data.remote.models.FilmDto
import com.pavelshelkovenko.shelkovenko.data.remote.models.FilmsResponse
import com.pavelshelkovenko.shelkovenko.data.remote.models.GenreDto
import com.pavelshelkovenko.shelkovenko.domain.FilmsRepository
import com.pavelshelkovenko.shelkovenko.domain.models.Film
import com.pavelshelkovenko.shelkovenko.domain.models.FilmDetails
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FilmsRepositoryImplTest {

    private lateinit var repository: FilmsRepository
    private val apiService: ApiService = mockk()
    private val cacheDao: CacheDao = mockk()
    private val mapper: FilmMapper = mockk()
    private val gson: Gson = mockk()

    @Before
    fun setup() {
        repository = FilmsRepositoryImpl(apiService, cacheDao, mapper, gson)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun test_getPopularFilms_returns_success_result_from_network() = runTest {
        // Arrange
        val validFilmResponse = FilmsResponse(
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
        coEvery {
            apiService.getTopFilms()
        } returns validFilmResponse
        val listAfterMapping = listOf(
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
        every { mapper.mapListDtoToDomainList(validFilmResponse.films!!) } returns listAfterMapping
        every { gson.toJson(validFilmResponse) } returns ""
        coEvery { cacheDao.putCache(any()) } just runs
        val expected = Result.success(listAfterMapping)

        // Act
        val actual = repository.getPopularFilms()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        coVerify { cacheDao.putCache(any()) }
    }

    @Test
    fun test_getPopularFilms_returns_failure_result_from_network_when_null() = runTest {
        // Arrange
        val nullResponse = FilmsResponse(null)
        coEvery { apiService.getTopFilms() } returns nullResponse
        val expected = Result.failure<List<Film>>(IllegalStateException("Empty response"))

        // Act
        val actual = repository.getPopularFilms()

        // Assert
        Truth.assertThat(actual.isFailure).isEqualTo(expected.isFailure)
        coVerify { cacheDao wasNot Called }
    }


    @Test
    fun test_getPopularFilms_returns_success_cached_result_when_any_exception() = runTest {
        // Arrange
        val validFilmResponse = FilmsResponse(
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
        coEvery { apiService.getTopFilms() } throws Exception()
        val cacheEntity: CacheEntity = mockk()
        coEvery { cacheDao.getCache(ApiService.TOP_100_POPULAR_FILMS_REQUEST) } returns cacheEntity
        val stringResponse: String = "mockk"
        every { cacheEntity.response } returns stringResponse
        every { gson.fromJson(stringResponse, FilmsResponse::class.java) } returns validFilmResponse
        val listAfterMapping = listOf(
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
        every { mapper.mapListDtoToDomainList(validFilmResponse.films!!) } returns listAfterMapping
        val expected = Result.success(listAfterMapping)

        // Act
        val actual = repository.getPopularFilms()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun test_getPopularFilms_returns_failure_when_cache_is_empty() = runTest {
        // Arrange
        coEvery { apiService.getTopFilms() } throws Exception()
        coEvery { cacheDao.getCache(ApiService.TOP_100_POPULAR_FILMS_REQUEST) } returns null

        val expected = Result.failure<List<Film>>(IllegalStateException("Empty cache"))
        // Act
        val actual = repository.getPopularFilms()

        // Assert
        Truth.assertThat(actual.isFailure).isEqualTo(expected.isFailure)
    }

    @Test
    fun test_getPopularFilms_returns_failure_when_gson_throws_exception() = runTest {
        // Arrange
        coEvery { apiService.getTopFilms() } throws Exception()
        val cacheEntity: CacheEntity = mockk()
        coEvery { cacheDao.getCache(ApiService.TOP_100_POPULAR_FILMS_REQUEST) } returns cacheEntity
        val stringResponse: String = "mockk"
        every { cacheEntity.response } returns stringResponse
        every { gson.fromJson(stringResponse, FilmsResponse::class.java) } throws Exception()

        val expected = Result.failure<List<Film>>(IllegalStateException("Empty response"))

        // Act
        val actual = repository.getPopularFilms()

        // Assert
        Truth.assertThat(actual.isFailure).isEqualTo(expected.isFailure)
    }


    @Test
    fun test_getFilmDetails_returns_success_when_valid_response() = runTest {
        // Arrange
        val id = 999
        val validFilmResponse = FilmDetailsResponse(
            id = 1,
            title = "1",
            posterUrl = "poster",
            description = "description",
            genres = listOf(GenreDto("genre")),
            year = 1,
            countries = listOf(CountryDto("country"))
        )
        coEvery {
            apiService.getFilmDetails(id)
        } returns validFilmResponse
        val mapResult = FilmDetails(
            id = 1,
            title = "title1",
            year = 1,
            posterUrl = "poster1",
            genre = listOf("genre1"),
            countries = listOf("country1"),
            description = "description",
        )

        every { mapper.mapFilmDetailsDtoToDomain(validFilmResponse) } returns mapResult
        every { gson.toJson(validFilmResponse) } returns ""
        coEvery { cacheDao.putCache(any()) } just runs
        val expected = Result.success(mapResult)

        // Act
        val actual = repository.getFilmDetails(id)

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        coVerify { cacheDao.putCache(any()) }
    }

    @Test
    fun test_getFilmDetails_returns_success_from_cache_when_exception() = runTest {
        // Arrange
        val id = 999
        coEvery {
            apiService.getFilmDetails(id)
        } throws Exception()
        // Arrange
        val validFilmResponse = FilmDetailsResponse(
            id = 1,
            title = "1",
            posterUrl = "poster",
            description = "description",
            genres = listOf(GenreDto("genre")),
            year = 1,
            countries = listOf(CountryDto("country"))
        )
        val requestPath = "${ApiService.BASE_URL}${ApiService.FILMS_PATH}/$id"
        val cacheEntity: CacheEntity = mockk()
        coEvery { cacheDao.getCache(requestPath) } returns cacheEntity
        val stringResponse: String = "mockk"
        every { cacheEntity.response } returns stringResponse
        every { gson.fromJson(stringResponse, FilmDetailsResponse::class.java) } returns validFilmResponse
        val mapResult = FilmDetails(
            id = 1,
            title = "title1",
            year = 1,
            posterUrl = "poster1",
            genre = listOf("genre1"),
            countries = listOf("country1"),
            description = "description",
        )
        every { mapper.mapFilmDetailsDtoToDomain(validFilmResponse) } returns mapResult
        val expected = Result.success(mapResult)

        // Act
        val actual = repository.getFilmDetails(id)

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun test_getFilmDetails_returns_failure_when_network_exception_and_cache_empty() = runTest {
        // Arrange
        val id = 999
        coEvery {
            apiService.getFilmDetails(id)
        } throws Exception()
        // Arrange

        val requestPath = "${ApiService.BASE_URL}${ApiService.FILMS_PATH}/$id"
        coEvery { cacheDao.getCache(requestPath) } returns null

        val expected = Result.failure<FilmDetails>(IllegalStateException("Empty cache"))

        // Act
        val actual = repository.getFilmDetails(id)

        // Assert
        Truth.assertThat(actual.isFailure).isEqualTo(expected.isFailure)
    }

    @Test
    fun test_getFilmDetails_returns_failure_when_network_exception_and_wrong_cache() = runTest {
        // Arrange
        val id = 999
        coEvery {
            apiService.getFilmDetails(id)
        } throws Exception()
        val requestPath = "${ApiService.BASE_URL}${ApiService.FILMS_PATH}/$id"
        val cacheEntity: CacheEntity = mockk()
        coEvery { cacheDao.getCache(requestPath) } returns cacheEntity
        val stringResponse: String = "mockk"
        every { cacheEntity.response } returns stringResponse
        every { gson.fromJson(stringResponse, FilmDetailsResponse::class.java) } throws Exception()

        val expected = Result.failure<FilmDetails>(exception = IllegalStateException("Wrong json"))

        // Act
        val actual = repository.getFilmDetails(id)

        // Assert
        Truth.assertThat(actual.isFailure).isEqualTo(expected.isFailure)
    }

    @Test
    fun test_searchFilmByKeyword_returns_success_when_good_response() = runTest {
        // Arrange
        val query = "query"
        val validFilmsResponse = FilmsResponse(
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
        coEvery { apiService.searchFilm(query) } returns validFilmsResponse
        val mappedFilms = listOf(
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
        every { mapper.mapListDtoToDomainList(validFilmsResponse.films!!) } returns mappedFilms
        val expected = Result.success(mappedFilms)

        // Act
        val actual = repository.searchFilmByKeyword(query)

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun test_searchFilmByKeyword_returns_failure_when_empty_response() = runTest {
        // Arrange
        val query = "query"
        val validFilmsResponse = FilmsResponse(
            films = null
        )
        coEvery { apiService.searchFilm(query) } returns validFilmsResponse
        val expected = Result.failure<List<Film>>(IllegalStateException("Empty response"))

        // Act
        val actual = repository.searchFilmByKeyword(query)

        // Assert
        Truth.assertThat(actual.isFailure).isEqualTo(expected.isFailure)
    }

    @Test
    fun test_searchFilmByKeyword_returns_failure_when_exception_throws() = runTest {
        // Arrange
        val query = "query"
        coEvery { apiService.searchFilm(query) } throws Exception()
        val expected = Result.failure<List<Film>>(Exception())

        // Act
        val actual = repository.searchFilmByKeyword(query)

        // Assert
        Truth.assertThat(actual.isFailure).isEqualTo(expected.isFailure)
    }
}