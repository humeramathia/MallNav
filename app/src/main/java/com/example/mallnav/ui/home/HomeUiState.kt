package com.example.mallnav.ui.home

import com.example.mallnav.data.model.Mall

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data object Empty : HomeUiState()
    data class Results(val malls: List<Mall>) : HomeUiState()
    data class NoResults(val query: String) : HomeUiState()
}
