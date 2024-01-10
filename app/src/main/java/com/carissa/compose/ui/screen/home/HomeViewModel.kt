package com.carissa.compose.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissa.compose.data.SeaAnimalsRepository
import com.carissa.compose.model.SeaAnimals
import com.carissa.compose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: SeaAnimalsRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<SeaAnimals>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<SeaAnimals>>>
        get() = _uiState

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) = viewModelScope.launch {
        _query.value = newQuery
        repository.searchSeaAnimals(_query.value)
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect {
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateListSeaAnimals(id: Int, newState: Boolean) = viewModelScope.launch {
        repository.updateListItems(id, newState)
            .collect { isUpdated ->
                if (isUpdated) search(_query.value)
            }
    }
}