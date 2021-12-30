package com.example.recipe_ghh.room

import androidx.room.*

@Entity(tableName = "recipe")
data class Recipe(
    var author: String,
    var ingredients: String,
    var instructions: String,
    @PrimaryKey(autoGenerate = true)
    var pk: Int,
    var title: String
)
