package com.pavelshelkovenko.shelkovenko.presentation.search_films

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.shelkovenko.domain.FilmsRepository
import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFilmsViewModel @Inject constructor(
    private val filmsRepository: FilmsRepository
) : ViewModel() {

    var state: MutableStateFlow<SearchFilmsScreenState> = MutableStateFlow((SearchFilmsScreenState()))
        private set

    var area: MutableStateFlow<SearchArea> = MutableStateFlow(SearchArea.Populars)
        private set

    private var searchJob: Job? = null

    fun onEvent(event: SearchFilmsScreenEvent) {
        when (event) {
            is SearchFilmsScreenEvent.ChangeKeyword -> {
                state.update { it.copy(keyword = event.newKeyword) }

                if (event.newKeyword.isNotBlank()) {
                    when (area.value) {
                        SearchArea.Populars -> {
                            searchPopularsFilms(event.newKeyword)
                        }
                        SearchArea.Favorites -> TODO()
                    }
                } else {
                    state.update {
                        it.copy(
                            isEmptySearch = true,
                            isLoading = false,
                            isError = false,
                            films = emptyList()
                        )
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

                    SearchArea.Favorites -> TODO()
                }
            }
        }
    }

    private fun searchPopularsFilms(newKeyword: String) {
        viewModelScope.launch {
            searchJob?.cancel()
            delay(800)
            searchJob = launch {
                ensureActive()
                state.update {
                    it.copy(
                        isInitial = false,
                        isLoading = true,
                        isError = false,
                        isEmptySearch = false
                    )
                }
                val result = filmsRepository.searchFilmByKeyword(keyword = newKeyword)
                ensureActive()
                result.onSuccess { films ->
                    if (films.isEmpty()) {
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
                                films = films.map { film ->
                                    FilmUi(
                                        id = film.id,
                                        title = film.title,
                                        year = film.year,
                                        posterUrl = film.posterUrl,
                                        genre = film.genre.firstOrNull() ?: "",
                                        isFavorite = film.isFavorite
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

    private fun searchFavoriteFilms(newKeyword: String) {

    }

    private fun defineSearchArea(newArea: SearchArea) {
        area.value = newArea
    }

}