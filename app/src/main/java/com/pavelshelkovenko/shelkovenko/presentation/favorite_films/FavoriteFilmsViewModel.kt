package com.pavelshelkovenko.shelkovenko.presentation.favorite_films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.shelkovenko.domain.FavoriteFilmsRepository
import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteFilmsViewModel @Inject constructor(
    private val repository: FavoriteFilmsRepository
) : ViewModel() {
    fun deleteFromFavorite(filmId: Int) {
        viewModelScope.launch {
            repository.deleteFavoriteFilm(filmId = filmId)
        }
    }

    var state: MutableStateFlow<FavoriteFilmsScreenState> =
        MutableStateFlow((FavoriteFilmsScreenState.Loading))
        private set

    init {
        viewModelScope.launch {
            repository.getFavoriteFilmsFlow().collect { list ->
                state.update {
                    FavoriteFilmsScreenState.Content(
                        favoriteFilms = list.map { film ->
                            FilmUi(
                                id = film.id,
                                title = film.title,
                                year = film.year,
                                posterUrl = film.posterUrl,
                                genre = film.genre.firstOrNull() ?: "",
                                isFavorite = film.isFavorite,
                                countries = film.countries
                            )
                        }
                    )
                }
            }
        }
    }

}