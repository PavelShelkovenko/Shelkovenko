package com.pavelshelkovenko.shelkovenko.presentation.popular_films

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pavelshelkovenko.shelkovenko.R
import com.pavelshelkovenko.shelkovenko.presentation.components.ButtonStatus
import com.pavelshelkovenko.shelkovenko.presentation.components.ComboButton
import com.pavelshelkovenko.shelkovenko.presentation.components.ErrorScreen
import com.pavelshelkovenko.shelkovenko.presentation.components.FilmsList
import com.pavelshelkovenko.shelkovenko.presentation.components.ShimmersFilmsList
import com.pavelshelkovenko.shelkovenko.presentation.components.TopBar
import com.pavelshelkovenko.shelkovenko.presentation.search_films.SearchArea

@Composable
fun PopularFilmsScreen(
    onNavigateToFilmDetails: (Int) -> Unit,
    onNavigateToFavoriteFilms: () -> Unit,
    onNavigateToSearchFilms: (SearchArea) -> Unit
) {

    val viewModel = hiltViewModel<PopularFilmsViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.downloadFilms()
    }

    PopularFilmContent(
        viewModel = viewModel,
        state = state,
        onNavigateToFilmDetails = onNavigateToFilmDetails,
        onNavigateToFavoriteFilms = onNavigateToFavoriteFilms,
        onNavigateToSearchFilms = onNavigateToSearchFilms
    )
}

@Composable
fun PopularFilmContent(
    viewModel: PopularFilmsViewModel,
    state: State<PopularFilmsScreenState>,
    onNavigateToFilmDetails: (Int) -> Unit,
    onNavigateToFavoriteFilms: () -> Unit,
    onNavigateToSearchFilms: (SearchArea) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        Column {

            TopBar(
                title = stringResource(id = R.string.popular)
            ) {
                onNavigateToSearchFilms(SearchArea.Populars)
            }

            when (val localState = state.value) {
                is PopularFilmsScreenState.Content -> {
                    FilmsList(
                        filmsList = localState.films,
                        contentPadding = PaddingValues(top = 10.dp, bottom = 70.dp),
                        onFilmClick = { filmId ->
                            onNavigateToFilmDetails(filmId)
                        },
                        onLongClickFilm = { film ->
                            viewModel.toggleFavorite(film)
                        }
                    )
                }

                is PopularFilmsScreenState.Loading -> {
                    ShimmersFilmsList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }

                is PopularFilmsScreenState.Error -> {
                    ErrorScreen {
                        viewModel.downloadFilms()
                    }
                }

            }
        }
        ComboButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            firstButtonTitle = stringResource(id = R.string.popular),
            secondButtonTitle = stringResource(id = R.string.favorite),
            firstButtonStatus = ButtonStatus.Selected,
            secondButtonStatus = ButtonStatus.Unselected,
            firstButtonClick = { },
            secondButtonClick = { onNavigateToFavoriteFilms() }
        )
    }
}