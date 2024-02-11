package com.pavelshelkovenko.shelkovenko.presentation.favorite_films

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import com.pavelshelkovenko.shelkovenko.presentation.popular_films.PopularFilmsScreenState
import com.pavelshelkovenko.shelkovenko.presentation.search_films.SearchArea

@Composable
fun FavoriteFilmsScreen(
    onNavigateToPopularFilms: () -> Unit,
    onNavigateToSearchFilms: (SearchArea) -> Unit,
    onNavigateToFilmDetails: (Int) -> Unit,
) {
    val viewModel = hiltViewModel<FavoriteFilmsViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()

    FavoriteFilmsContent(
        viewModel = viewModel,
        state = state,
        onNavigateToFilmDetails = { filmId ->
            onNavigateToFilmDetails(filmId)
        },
        onNavigateToPopularFilms = { onNavigateToPopularFilms() },
        onNavigateToSearchFilms = { onNavigateToSearchFilms(it) }
    )
}

@Composable
fun FavoriteFilmsContent(
    viewModel: FavoriteFilmsViewModel,
    state: State<FavoriteFilmsScreenState>,
    onNavigateToFilmDetails: (Int) -> Unit,
    onNavigateToPopularFilms: () -> Unit,
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
                title = stringResource(id = R.string.favorite)
            ) {
                onNavigateToSearchFilms(SearchArea.Favorites)
            }

            when (val localState = state.value) {
                is FavoriteFilmsScreenState.Content -> {
                    FilmsList(
                        filmsList = localState.favoriteFilms,
                        contentPadding = PaddingValues(top = 10.dp, bottom = 70.dp)
                    ) { filmId ->
                        onNavigateToFilmDetails(filmId)
                    }
                }

                is FavoriteFilmsScreenState.Loading -> {
                    ShimmersFilmsList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }

                is FavoriteFilmsScreenState.Error -> {
                    ErrorScreen {
                        //viewModel.downloadFilms()
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
            firstButtonStatus = ButtonStatus.Unselected,
            secondButtonStatus = ButtonStatus.Selected,
            firstButtonClick = { onNavigateToPopularFilms() },
            secondButtonClick = { }
        )
    }
}