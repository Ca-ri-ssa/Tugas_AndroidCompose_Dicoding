package com.carissa.compose.model

data class SeaAnimals (
    val id: Int,
    val name: String,
    val description: String,
    val latinName: String,
    val imgUrl: String,
    val funFact: String,
    var isStarred: Boolean = false,
)