package com.pavelshelkovenko.shelkovenko.presentation.favorite_films

import androidx.lifecycle.ViewModel
import com.pavelshelkovenko.shelkovenko.domain.FavoriteFilmsRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FavoriteFilmsViewModel @Inject constructor(
    private val repository: FavoriteFilmsRepository
): ViewModel() {

    var state: MutableStateFlow<FavoriteFilmsScreenState> = MutableStateFlow((FavoriteFilmsScreenState.Loading))
        private set



}