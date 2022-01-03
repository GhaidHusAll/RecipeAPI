package com.example.recipe_ghh.room

import androidx.room.*

@Entity(tableName = "recipe")
data class Recipe(
    var author: String,
    var ingredients: String,
    var instructions: String,
    @PrimaryKey
    var pk: String,
    var title: String
)
