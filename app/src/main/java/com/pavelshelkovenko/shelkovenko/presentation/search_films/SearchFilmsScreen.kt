package com.pavelshelkovenko.shelkovenko.presentation.search_films

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pavelshelkovenko.shelkovenko.R
import com.pavelshelkovenko.shelkovenko.presentation.components.BackArrowButton
import com.pavelshelkovenko.shelkovenko.presentation.components.ErrorScreen
import com.pavelshelkovenko.shelkovenko.presentation.components.FilmsList

@Composable
fun SearchFilmsScreen(
    area: SearchArea,
    onBackPressed: () -> Unit,
    onNavigateToFilmDetails: (Int) -> Unit
) {
    val viewModel = hiltViewModel<SearchFilmsViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(SearchFilmsScreenEvent.DefineSearchArea(area = area))
    }

    SearchFilmsContent(
        viewModel = viewModel,
        state = state,
        onBackPressed = onBackPressed,
        onKeywordChanged = { newKeyword ->
            viewModel.onEvent(SearchFilmsScreenEvent.ChangeKeyword(newKeyword))
        },
        onNavigateToFilmDetails = onNavigateToFilmDetails
    )
}

@Composable
fun SearchFilmsContent(
    viewModel: SearchFilmsViewModel,
    state: State<SearchFilmsScreenState>,
    onKeywordChanged: (String) -> Unit,
    onBackPressed: () -> Unit,
    onNavigateToFilmDetails: (Int) -> Unit
) {
    val localState = state.value
    val area = viewModel.area.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .padding(vertical = 28.dp, horizontal = 12.dp)
    ) {
        SearchFilmsHeader(
            keyword = localState.keyword,
            onBackPressed = { onBackPressed() },
            onKeywordChanged = { newKeyword -> onKeywordChanged(newKeyword) },
            area = area.value
        )
        when {
            localState.isInitial -> {

            }

            localState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            localState.isEmptySearch -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = stringResource(id = R.string.not_found),
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            localState.isError -> {
                ErrorScreen {
                    viewModel.onEvent(event = SearchFilmsScreenEvent.Repeat)
                }
            }

            else -> {
                FilmsList(
                    filmsList = localState.films,
                    contentPadding = PaddingValues(top = 10.dp, bottom = 20.dp),
                    onFilmClick = { filmId ->
                        onNavigateToFilmDetails(filmId)
                    },
                    onLongClickFilm = { filmUi ->
                        viewModel.toggleFavorite(filmUi)
                    }
                )
            }
        }
    }
}

@Composable
fun SearchFilmsHeader(
    keyword: String,
    onBackPressed: () -> Unit,
    onKeywordChanged: (String) -> Unit,
    area: SearchArea
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackArrowButton(
            modifier = Modifier,
            onBackPressed = { onBackPressed() }
        )
        TextField(
            value = keyword,
            onValueChange = { onKeywordChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            placeholder = {
                Text(
                    text = when (area) {
                        SearchArea.Populars -> stringResource(id = R.string.search_for_populars)
                        SearchArea.Favorites -> stringResource(id = R.string.search_for_favorite)
                    },
                    style = MaterialTheme.typography.displayMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                unfocusedTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                focusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.background,
            ),
            trailingIcon = {
                if (keyword != "") {
                    IconButton(onClick = { onKeywordChanged("") }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            singleLine = true,
            maxLines = 1,
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}