package com.example.recipe_ghh.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipe_ghh.room.Recipe
import com.example.recipe_ghh.room.RecipeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class RecipeViewModel: ViewModel() {

    private val recipes : MutableLiveData<List<Recipe>> = MutableLiveData()

    init{
        recipes.postValue(listOf())
    }
    fun getRecipes(dao: RecipeDao):LiveData<List<Recipe>>{
        CoroutineScope(IO).launch {
            val data = async {
                dao.getRecipes()
            }.await()
            if (data.isNotEmpty()) {
                recipes.postValue(data)
            } else {
                Log.e("MAIN", "Unable to get data",)
            }
        }
        return recipes
    }
    suspend fun updateRecipes(recipe:Recipe, dao: RecipeDao):Int{
        var isUpdate = 0
       val result = CoroutineScope(IO).launch {
            isUpdate =  dao.updateRecipe(recipe)
            recipes.postValue(dao.getRecipes())
        }
        result.join()
        return isUpdate
    }
    suspend fun deleteRecipes(recipe:Recipe, dao: RecipeDao):Int{
        var isDelete = 0
        val result = CoroutineScope(IO).launch {
            isDelete =  dao.deleteRecipe(recipe)
            recipes.postValue(dao.getRecipes())
        }
        result.join()
        return isDelete
    }
    suspend fun addRecipes(recipe:Recipe, dao: RecipeDao):Long{
        var isAdd = 0L
        val result = CoroutineScope(IO).launch {
            isAdd =  dao.addRecipe(recipe)
            recipes.postValue(dao.getRecipes())
        }
        result.join()
        return isAdd
    }
}