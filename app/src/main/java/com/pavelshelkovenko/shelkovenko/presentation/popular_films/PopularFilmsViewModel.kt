package com.pavelshelkovenko.shelkovenko.presentation.popular_films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.shelkovenko.domain.GetPopularFilmsUseCase
import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularFilmsViewModel @Inject constructor(
    private val getPopularFilmsUseCase: GetPopularFilmsUseCase,
): ViewModel() {

    var state: MutableStateFlow<PopularFilmsScreenState> = MutableStateFlow((PopularFilmsScreenState.Loading))
        private set

    init {
        downloadFilms()
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