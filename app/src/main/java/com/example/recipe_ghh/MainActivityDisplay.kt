package com.example.recipe_ghh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.recipe_ghh.api.API
import com.example.recipe_ghh.api.Client
import com.example.recipe_ghh.api.RecipeesItem
import com.example.recipe_ghh.databinding.ActivityMainDisplayBinding
import com.example.recipe_ghh.room.DatabaseApp
import com.example.recipe_ghh.room.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivityDisplay : AppCompatActivity() {
    private lateinit var binding : ActivityMainDisplayBinding
    private val daoForUD by lazy { DatabaseApp.getDB(this).myDao() }
    private var  recipeTitle = ""
    private var  recipeAuthor = ""
    private var  recipeIng = ""
    private var  recipeIns = ""
    private var  id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayInformation()
        binding.btnBack.setOnClickListener {
            backToMain()
        }
        binding.btnDelete.setOnClickListener {
            //deleteRecipeById(id)
            deleteRecipeRoom(Recipe(recipeAuthor,recipeIng,recipeIng,id,recipeTitle))
        }
        binding.btnEdit.setOnClickListener {
           // editRecipe(id)
            editRecipeRoom()
        }
    }

    private fun displayInformation(){
         id = intent.getIntExtra("recipeId",0)
        recipeTitle = intent.getStringExtra("recipeTitle").toString()
         recipeAuthor = intent.getStringExtra("recipeAuthor").toString()
         recipeIng = intent.getStringExtra("recipeIng").toString()
         recipeIns = intent.getStringExtra("recipeIns").toString()

        binding.tvTitle.setText(recipeTitle)
        binding.tvAuthor.setText(recipeAuthor)
        binding.tvIngredients.setText(recipeIng)
        binding.tvInstructions.setText(recipeIns)
    }

    private fun deleteRecipeById(id:Int){
        val api = Client().getTheClient()?.create(API::class.java)
        api?.deleteRecipe(id)?.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(this@MainActivityDisplay, "Recipe Deleted successfully",
                    Toast.LENGTH_LONG).show()
                backToMain()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivityDisplay, "Something went wrong not able to delete Recipe",
                    Toast.LENGTH_LONG).show()

            }

        })
    }
    private fun editRecipe(id: Int){
        val api = Client().getTheClient()?.create(API::class.java)
        api?.editRecipe(id, RecipeesItem(binding.tvAuthor.text.toString(),
            binding.tvIngredients.text.toString(),
            binding.tvInstructions.text.toString(),id,
            binding.tvTitle.text.toString())
        )?.enqueue(object: Callback<RecipeesItem>{
            override fun onResponse(call: Call<RecipeesItem>, response: Response<RecipeesItem>) {
                Toast.makeText(this@MainActivityDisplay, "Recipe Updated successfully",
                    Toast.LENGTH_LONG).show()
                backToMain()
            }

            override fun onFailure(call: Call<RecipeesItem>, t: Throwable) {
                Toast.makeText(this@MainActivityDisplay, "Something went wrong not able to update Recipe",
                    Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun deleteRecipeRoom(recipe:Recipe){
        CoroutineScope(IO).launch {
            val isDelete = daoForUD.deleteRecipe(recipe)
            withContext(Main){
                if (isDelete > 0){
                    Toast.makeText(this@MainActivityDisplay, "Recipe Deleted successfully",
                        Toast.LENGTH_LONG).show()
                    backToMain()
                }else{
                    Toast.makeText(this@MainActivityDisplay, "Something went wrong not able to delete Recipe",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun editRecipeRoom(){
       CoroutineScope(IO).launch {
          val isUpdate = daoForUD.updateRecipe(
              Recipe(binding.tvAuthor.text.toString(),
                  binding.tvIngredients.text.toString(),
                  binding.tvInstructions.text.toString(),id,
                  binding.tvTitle.text.toString())
          )
           withContext(Main){
               if (isUpdate > 0){
                   Toast.makeText(this@MainActivityDisplay, "Recipe Updated successfully",
                       Toast.LENGTH_LONG).show()
                   backToMain()
               }else{
                   Toast.makeText(this@MainActivityDisplay, "Something went wrong not able to update Recipe",
                       Toast.LENGTH_LONG).show()
               }
           }

       }

    }

    fun backToMain(){
        val backToMain = Intent(this,MainActivity::class.java)
        startActivity(backToMain)
    }
}