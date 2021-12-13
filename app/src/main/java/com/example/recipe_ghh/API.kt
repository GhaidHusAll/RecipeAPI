package com.example.recipe_ghh

import retrofit2.Call
import retrofit2.http.*

interface API {
    @GET("/recipes/")
    fun getRecipe(): Call<Recipes>

    @POST("/recipes/")
    fun addRecipe(@Body recipe: RecipeesItem): Call<RecipeesItem>
}