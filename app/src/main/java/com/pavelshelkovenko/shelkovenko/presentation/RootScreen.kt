package com.pavelshelkovenko.shelkovenko.presentation

import androidx.compose.runtime.Composable
import com.pavelshelkovenko.shelkovenko.navigation.AppNavGraph
import com.pavelshelkovenko.shelkovenko.navigation.rememberNavigationState
import com.pavelshelkovenko.shelkovenko.presentation.popular_films.PopularFilmsScreen

@Composable
fun RootScreen() {

    val navigationState = rememberNavigationState()

    AppNavGraph(
        navHostController = navigationState.navHostController,
        popularFilmsScreenContent = {
            PopularFilmsScreen()
        },
        favoriteFilmsScreenContent = { },
        filmDetailsScreenContent = { id ->

        },
        searchFilmsScreenContent = { }
    )
}