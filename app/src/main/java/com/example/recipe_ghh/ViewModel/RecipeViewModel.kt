package com.example.recipe_ghh.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.PrimaryKey
import com.example.recipe_ghh.room.Recipe
import com.example.recipe_ghh.room.RecipeDao
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class RecipeViewModel: ViewModel() {

    private val recipes : MutableLiveData<List<Recipe>> = MutableLiveData()
    val db = Firebase.firestore

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
    fun addRecipesFirebase(recipeInput: Recipe):Int{
        var returnNum = 1
        val recipe = hashMapOf(
        "author" to recipeInput.author,
        "ingredients" to recipeInput.ingredients,
        "instructions" to recipeInput.instructions,
        "title" to recipeInput.title
        )
        db.collection("recipes").add(recipe)
            .addOnSuccessListener { documentReference ->
                Log.d("VM","DocumentSnapshot added with ID: ${documentReference.id}")
                returnNum = 0
            }
            .addOnFailureListener { e ->
                Log.w("VM", "Error adding document", e) }
        return returnNum
    }
    fun getRecipesFirebase():LiveData<List<Recipe>>{
       db.collection("recipes").get()
           .addOnCompleteListener  { result ->
               if (result.isSuccessful) {
                   var data : MutableList<Recipe> = mutableListOf()
                   for (doc in result.result!!) {
                       Log.d("VM", "${doc.id} => ${doc.data}")
                       data.add(Recipe(
                           doc.data["author"].toString(),doc.data["ingredients"].toString(),
                           doc.data["instructions"].toString(),doc.id,
                           doc.data["title"].toString()
                       ))
                   }
                   recipes.postValue(data)
               }
           }
           .addOnFailureListener { exception ->
               Log.w("VM", "Error getting documents.", exception) }
        return recipes
    }
    fun updateRecipesFirebase(recipe:Recipe):Int{
        var isUpdate = 1
        db.collection("recipes").document(recipe.pk).set(recipe)
            .addOnSuccessListener { _ ->
                isUpdate = 0
                Log.d("TAG VM", "-----------------------DocumentSnapshot edit with ID: ${recipe.pk} ") }
            .addOnFailureListener {  e -> Log.w("TAG VM", "-------------------Error edit document", e) }

        return isUpdate
    }
     fun deleteRecipesFirebase(recipe:Recipe):Int{
        var isDelete = 1
         db.collection("recipes").document(recipe.pk).delete()
             .addOnSuccessListener {
                 isDelete = 0
                 Log.d("VMDelete"," Doc edited")
             }
             .addOnFailureListener { e -> Log.d("VMDelete","error $e") }
        return isDelete
    }
}