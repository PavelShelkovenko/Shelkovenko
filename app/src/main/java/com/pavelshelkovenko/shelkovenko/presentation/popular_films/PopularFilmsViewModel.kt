package com.pavelshelkovenko.shelkovenko.presentation.popular_films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.shelkovenko.domain.FavoriteFilmsRepository
import com.pavelshelkovenko.shelkovenko.domain.GetPopularFilmsUseCase
import com.pavelshelkovenko.shelkovenko.domain.models.Film
import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularFilmsViewModel @Inject constructor(
    private val getPopularFilmsUseCase: GetPopularFilmsUseCase,
    private val favoriteFilmsRepository: FavoriteFilmsRepository
) : ViewModel() {

    var state: MutableStateFlow<PopularFilmsScreenState> =
        MutableStateFlow((PopularFilmsScreenState.Loading))
        private set

    init {
        downloadFilms()
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
            val contentState = it as? PopularFilmsScreenState.Content
            contentState?.let { localState ->
                localState.copy(
                    films = localState.films.map { oldFilm ->
                        if (oldFilm.id == film.id) {
                            oldFilm.copy(
                                isFavorite = !oldFilm.isFavorite
                            )
                        } else {
                            oldFilm
                        }
                    }
                )
            } ?: it
        }
    }

    fun downloadFilms() {
        viewModelScope.launch {
            state.update { PopularFilmsScreenState.Loading }
            getPopularFilmsUseCase().onSuccess { films ->
                state.update {
                    PopularFilmsScreenState.Content(
                        films = films.map { film ->
                            FilmUi(
                                id = film.id,
                                title = film.title,
                                year = film.year,
                                posterUrl = film.posterUrl,
                                genre = film.genre.firstOrNull() ?: "",
                                countries = film.countries,
                                isFavorite = film.isFavorite
                            )
                        }
                    )
                }
            }.onFailure {
                state.update { PopularFilmsScreenState.Error }
            }
        }
    }
}