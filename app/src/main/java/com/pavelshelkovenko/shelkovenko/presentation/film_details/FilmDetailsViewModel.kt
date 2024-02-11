package com.pavelshelkovenko.shelkovenko.presentation.film_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.shelkovenko.domain.FilmsRepository
import com.pavelshelkovenko.shelkovenko.presentation.model.FilmDetailsUi
import com.pavelshelkovenko.shelkovenko.presentation.popular_films.PopularFilmsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmDetailsViewModel @Inject constructor(
    private val repository: FilmsRepository
) : ViewModel() {

    var state: MutableStateFlow<FilmDetailsScreenState> = MutableStateFlow((FilmDetailsScreenState.Loading))
        private set


    fun onEvent(event: FilmDetailsScreenEvent) {
        when(event) {
            is FilmDetailsScreenEvent.GetFilmDetails -> {
                getFilmDetails(event.filmId)
            }
        }
    }

    private fun getFilmDetails(filmId: Int) {
        viewModelScope.launch {
            state.update { FilmDetailsScreenState.Loading }
            repository.getFilmDetails(filmId = filmId).onSuccess { filmDetails ->
                state.update {
                    FilmDetailsScreenState.Content(
                        FilmDetailsUi(
                            id = filmDetails.id,
                            posterUrl = filmDetails.posterUrl,
                            title = filmDetails.title,
                            description = filmDetails.description,
                            genres = filmDetails.genre.joinToString(", "),
                            countries = filmDetails.countries.joinToString(", ")
                        )
                    )
                }
            }.onFailure {
                state.update { FilmDetailsScreenState.Error }
            }
        }
    }

}