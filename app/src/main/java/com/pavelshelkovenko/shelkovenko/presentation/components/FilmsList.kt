package com.pavelshelkovenko.shelkovenko.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi

@Composable
fun FilmsList(
    filmsList: List<FilmUi>,
    contentPadding: PaddingValues,
    onFilmClick: (Int) -> Unit,
    onLongClickFilm: (FilmUi) -> Unit,
) {
    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            filmsList,
            key = { film -> film.id }
        ) { film ->
            FilmCard(
                filmUi = film,
                onFilmClick = { filmId -> onFilmClick(filmId) },
                onLongClickFilm = onLongClickFilm
            )
        }
    }
}