package com.carissa.compose.data

import com.carissa.compose.model.SeaAnimals
import com.carissa.compose.model.SeaAnimalsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class SeaAnimalsRepository {
    private val dummySeaAnimals = mutableListOf<SeaAnimals>()

    init {
        if (dummySeaAnimals.isEmpty()) {
            SeaAnimalsData.seaAnimals.forEach {
                dummySeaAnimals.add(it)
            }
        }
    }

    fun getSeaAnimalsId(detailId: Int): SeaAnimals {
        return dummySeaAnimals.first {
            it.id == detailId
        }
    }

    fun getStarredItem(): Flow<List<SeaAnimals>> {
        return flowOf(dummySeaAnimals.filter { it.isStarred })
    }

    fun searchSeaAnimals(query: String) = flow {
        val data = dummySeaAnimals.filter {
            it.name.contains(query, ignoreCase = true)
        }
        emit(data)
    }

    fun updateListItems(detailId: Int, newState: Boolean): Flow<Boolean> {
        val index = dummySeaAnimals.indexOfFirst { it.id == detailId }
        val result = if (index >= 0) {
            val player = dummySeaAnimals[index]
            dummySeaAnimals[index] = player.copy(isStarred = newState)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    companion object {
        @Volatile
        private var instance: SeaAnimalsRepository? = null

        fun getInstance(): SeaAnimalsRepository =
            instance ?: synchronized(this) {
                SeaAnimalsRepository().apply {
                    instance = this
                }
            }
    }
}