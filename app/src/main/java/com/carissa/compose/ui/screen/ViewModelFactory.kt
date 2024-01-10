package com.carissa.compose.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carissa.compose.data.SeaAnimalsRepository
import com.carissa.compose.ui.screen.detail.DetailViewModel
import com.carissa.compose.ui.screen.home.HomeViewModel
import com.carissa.compose.ui.screen.starred.StarredViewModel

class ViewModelFactory(private val repository: SeaAnimalsRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(StarredViewModel::class.java)) {
            return StarredViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}