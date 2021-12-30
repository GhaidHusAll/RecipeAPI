package com.example.recipe_ghh.room

import androidx.room.*

@Dao
interface RecipeDao {

    @Insert
    suspend fun addRecipe(recipe:Recipe):Long

    @Query("SELECT * from recipe")
    suspend fun getRecipes(): List<Recipe>

    @Update
    suspend fun updateRecipe(recipe: Recipe):Int

    @Delete
    suspend fun deleteRecipe(recipe:Recipe):Int
}