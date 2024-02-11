package com.pavelshelkovenko.shelkovenko.presentation.film_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.pavelshelkovenko.shelkovenko.R
import com.pavelshelkovenko.shelkovenko.presentation.components.BackArrowButton
import com.pavelshelkovenko.shelkovenko.presentation.components.ErrorScreen
import com.pavelshelkovenko.shelkovenko.presentation.model.FilmDetailsUi

@Composable
fun FilmDetailsScreen(
    filmId: Int,
    onBackPressed: () -> Unit
) {
    val viewModel = hiltViewModel<FilmDetailsViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(event = FilmDetailsScreenEvent.GetFilmDetails(filmId = filmId))
    }

    FilmDetailsMainContent(
        viewModel = viewModel,
        state = state,
        filmId = filmId,
        onBackPressed = onBackPressed,
    )
}


@Composable
fun FilmDetailsMainContent(
    viewModel: FilmDetailsViewModel,
    state: State<FilmDetailsScreenState>,
    filmId: Int,
    onBackPressed: () -> Unit
) {
    when (val localState = state.value) {
        is FilmDetailsScreenState.Loading -> {
            BackArrowButton(
                modifier = Modifier.padding(top = 28.dp, start = 12.dp),
                onBackPressed = { onBackPressed() }
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        is FilmDetailsScreenState.Content -> {
            FilmDetailsContent(
                filmDetails = localState.filmDetails,
            ) {
                onBackPressed()
            }
        }

        is FilmDetailsScreenState.Error -> {
            BackArrowButton(
                modifier = Modifier.padding(top = 28.dp, start = 12.dp),
                onBackPressed = { onBackPressed() }
            )
            ErrorScreen {
                viewModel.onEvent(event = FilmDetailsScreenEvent.GetFilmDetails(filmId = filmId))
            }
        }
    }
}

@Composable
fun FilmDetailsContent(
    filmDetails: FilmDetailsUi,
    onBackPressed: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    AsyncImage(
                        model = filmDetails.posterUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_empty_image_placeholder),
                        error = painterResource(R.drawable.ic_empty_image_placeholder),
                    )
                }
            }
            item {
                FilmDetailsDescription(
                    modifier = Modifier,
                    filmDetails = filmDetails
                )
            }
        }

        BackArrowButton(
            modifier = Modifier.padding(top = 24.dp, start = 12.dp),
            onBackPressed = { onBackPressed() }
        )
    }
}

@Composable
fun FilmDetailsDescription(
    modifier: Modifier = Modifier,
    filmDetails: FilmDetailsUi
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Text(
            text = filmDetails.title,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = filmDetails.description,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "${stringResource(id = R.string.genres)}: ${filmDetails.genres}",
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "${stringResource(id = R.string.countries)}: ${filmDetails.countries}",
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 16.sp
        )
    }
}

@Preview
@Composable
fun PreviewFilmDetailsContent() {
    FilmDetailsContent(
        filmDetails = FilmDetailsUi(
            id = 6151,
            posterUrl = "http://www.bing.com/search?q=facilis",
            title = "deseruisse",
            description = "errem",
            genres = "odio",
            countries = "delicata"
        ),
    ) {}
}

