package com.carissa.compose.ui.screen.starred

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissa.compose.data.SeaAnimalsRepository
import com.carissa.compose.model.SeaAnimals
import com.carissa.compose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StarredViewModel(
    private val repository: SeaAnimalsRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<SeaAnimals>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<SeaAnimals>>> = _uiState

    fun getStarredItem() = viewModelScope.launch {
        repository.getStarredItem()
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect {
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateListSeaAnimals(id: Int, newState: Boolean) {
        repository.updateListItems(id, newState)
        getStarredItem()
    }
}