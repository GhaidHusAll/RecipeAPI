package com.example.recipe_ghh.api

import retrofit2.Call
import retrofit2.http.*

interface API {
    @GET("/recipes/")
    fun getRecipe(): Call<Recipes>

    @POST("/recipes/")
    fun addRecipe(@Body recipe: RecipeesItem): Call<RecipeesItem>

    @DELETE("/recipes/{id}")
    fun deleteRecipe(@Path ("id") id: Int ):Call<Void>

    @PUT("/recipes/{id}")
    fun editRecipe(@Path("id") id: Int, @Body recipe: RecipeesItem):Call<RecipeesItem>
}