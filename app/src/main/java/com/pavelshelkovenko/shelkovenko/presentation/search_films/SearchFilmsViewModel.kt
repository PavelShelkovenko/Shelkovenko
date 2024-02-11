package com.pavelshelkovenko.shelkovenko.presentation.search_films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.shelkovenko.domain.FavoriteFilmsRepository
import com.pavelshelkovenko.shelkovenko.domain.FilmsRepository
import com.pavelshelkovenko.shelkovenko.domain.models.Film
import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFilmsViewModel @Inject constructor(
    private val filmsRepository: FilmsRepository,
    private val favoriteFilmsRepository: FavoriteFilmsRepository
) : ViewModel() {

    var state: MutableStateFlow<SearchFilmsScreenState> =
        MutableStateFlow((SearchFilmsScreenState()))
        private set

    var area: MutableStateFlow<SearchArea> = MutableStateFlow(SearchArea.Populars)
        private set

    private var searchJob: Job? = null

    fun onEvent(event: SearchFilmsScreenEvent) {
        when (event) {
            is SearchFilmsScreenEvent.ChangeKeyword -> {
                state.update { it.copy(keyword = event.newKeyword) }
                when (area.value) {
                    SearchArea.Populars -> {
                        searchPopularsFilms(event.newKeyword)
                    }

                    SearchArea.Favorites -> {
                        searchFavoriteFilms(event.newKeyword)
                    }
                }
            }

            is SearchFilmsScreenEvent.DefineSearchArea -> {
                defineSearchArea(event.area)
            }

            is SearchFilmsScreenEvent.Repeat -> {
                when (area.value) {
                    SearchArea.Populars -> {
                        searchPopularsFilms(state.value.keyword)
                    }

                    SearchArea.Favorites -> {
                        searchFavoriteFilms(state.value.keyword)
                    }
                }
            }
        }
    }

    private fun searchFavoriteFilms(query: String) {
        state.update {
            it.copy(
                isLoading = true,
                isInitial = false,
                isEmptySearch = false,
                isError = false,
            )
        }
        viewModelScope.launch {
            val result = favoriteFilmsRepository.searchFavoriteFilms(query = query)
            state.update {
                it.copy(
                    films = result.map { film ->
                        FilmUi(
                            id = film.id,
                            title = film.title,
                            year = film.year,
                            posterUrl = film.posterUrl,
                            genre = film.genre.firstOrNull() ?: "",
                            isFavorite = film.isFavorite,
                            countries = film.countries
                        )
                    },
                    isLoading = false,
                    isEmptySearch = false,
                    isError = false,
                    isInitial = false
                )
            }
        }
    }

    private fun searchPopularsFilms(newKeyword: String) {
        viewModelScope.launch {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(800)
                state.update {
                    it.copy(
                        isInitial = false,
                        isLoading = true,
                        isError = false,
                        isEmptySearch = false
                    )
                }
                val result = filmsRepository.searchFilmByKeyword(keyword = newKeyword)
                result.onSuccess { films ->
                    val mappedResult = mapWithFavorite(films)
                    if (mappedResult.isEmpty()) {
                        state.update {
                            it.copy(
                                isLoading = false,
                                isEmptySearch = true,
                                isError = false,
                                films = emptyList()
                            )
                        }
                    } else {
                        state.update {
                            it.copy(
                                films = mappedResult.map { film ->
                                    FilmUi(
                                        id = film.id,
                                        title = film.title,
                                        year = film.year,
                                        posterUrl = film.posterUrl,
                                        genre = film.genre.firstOrNull() ?: "",
                                        isFavorite = film.isFavorite,
                                        countries = film.countries
                                    )
                                },
                                isLoading = false,
                                isEmptySearch = false,
                                isError = false
                            )
                        }
                    }
                }.onFailure {
                    state.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            isEmptySearch = false
                        )
                    }
                }
            }
        }
    }

    private suspend fun mapWithFavorite(films: List<Film>): List<Film> {
        val favoriteFilmsList = favoriteFilmsRepository.getFavoriteFilms()
        return films.map { popularFilm ->
            if (favoriteFilmsList.find { popularFilm.id == it.id } != null) {
                popularFilm.copy(
                    isFavorite = true
                )
            } else {
                popularFilm
            }
        }
    }

    private fun defineSearchArea(newArea: SearchArea) {
        area.value = newArea
    }

    fun toggleFavorite(film: FilmUi) {
        viewModelScope.launch {
            if (film.isFavorite) {
                favoriteFilmsRepository.deleteFavoriteFilm(filmId = film.id)
            } else {
                favoriteFilmsRepository.addFavoriteFilm(
                    Film(
                        id = film.id,
                        title = film.title,
                        year = film.year,
                        genre = film.genre.split(','),
                        posterUrl = film.posterUrl,
                        countries = film.countries,
                        isFavorite = true
                    )
                )
            }
        }
        state.update {
            it.copy(
                films = it.films.map { oldFilm ->
                    if (oldFilm.id == film.id) {
                        oldFilm.copy(
                            isFavorite = !oldFilm.isFavorite
                        )
                    } else {
                        oldFilm
                    }
                }
            )
        }
    }
}










