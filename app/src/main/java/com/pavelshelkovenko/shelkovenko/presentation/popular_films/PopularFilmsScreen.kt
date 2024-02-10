package com.pavelshelkovenko.shelkovenko.presentation.popular_films

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
import com.pavelshelkovenko.shelkovenko.presentation.components.FilmsList
import com.pavelshelkovenko.shelkovenko.presentation.components.ShimmersFilmsList
import com.pavelshelkovenko.shelkovenko.presentation.components.TopBar

@Composable
fun PopularFilmsScreen() {

    val viewModel = hiltViewModel<PopularFilmsViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()

    PopularFilmContent(
        viewModel = viewModel,
        state = state
    )
}

@Composable
fun PopularFilmContent(
    viewModel: PopularFilmsViewModel,
    state: State<PopularFilmsScreenState>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        Column {

            TopBar(title = stringResource(id = R.string.popular)) {
                // navigation to search
            }

            when (val localState = state.value) {
                is PopularFilmsScreenState.Content -> {
                    FilmsList(
                        filmsList = localState.films,
                        contentPadding = PaddingValues(top = 10.dp, bottom = 70.dp)
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
                        // try to repeat downloading data
                    }
                }

            }
        }
        if (state.value != PopularFilmsScreenState.Error) {
            ComboButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                firstButtonTitle = stringResource(id = R.string.popular),
                secondButtonTitle = stringResource(id = R.string.favorite),
                firstButtonStatus = ButtonStatus.Selected,
                secondButtonStatus = ButtonStatus.Unselected,
                firstButtonClick = { },
                secondButtonClick = { }
            )
        }
    }
}