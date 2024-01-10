package com.carissa.compose.di

import com.carissa.compose.data.SeaAnimalsRepository

object Injection {
    fun provideRepository(): SeaAnimalsRepository {
        return SeaAnimalsRepository.getInstance()
    }
}