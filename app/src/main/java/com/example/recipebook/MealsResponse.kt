package com.example.recipebook
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



data class Meal(
    val name: String,
    val imageUrl: String,
    val id: String
)

data class Recipe(

    val name: String,
    val imageUrl:String,
    val instructions:String,
    val id: String
)
