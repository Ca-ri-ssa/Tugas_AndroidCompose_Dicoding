package com.carissa.compose.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissa.compose.data.SeaAnimalsRepository
import com.carissa.compose.model.SeaAnimals
import com.carissa.compose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: SeaAnimalsRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<SeaAnimals>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<SeaAnimals>>
        get() = _uiState

    fun getSeaAnimalsId(id: Int) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        _uiState.value = UiState.Success(repository.getSeaAnimalsId(id))
    }


    fun updateListSeaAnimals(id: Int, newState: Boolean) = viewModelScope.launch {
        repository.updateListItems(id, !newState)
            .collect { isUpdated ->
                if (isUpdated) getSeaAnimalsId(id)
            }
    }
}
