package com.example.recipe_ghh.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Client {

    private var retrofitVar: Retrofit? = null

    fun getTheClient(): Retrofit? {
        retrofitVar = Retrofit.Builder().baseUrl("https://dojo-recipes.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitVar
    }

}