package com.example.recipe_ghh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.recipe_ghh.ViewModel.RecipeViewModel
import com.example.recipe_ghh.api.API
import com.example.recipe_ghh.api.Client
import com.example.recipe_ghh.api.RecipeesItem
import com.example.recipe_ghh.databinding.ActivityMainAddBinding
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

class MainActivityAdd : AppCompatActivity() {
    private lateinit var binding : ActivityMainAddBinding
    private val dao by lazy { DatabaseApp.getDB(this).myDao() }
    private val vm by lazy { ViewModelProvider(this).get(RecipeViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnAdd.setOnClickListener {
            if (checkInput()){
                addRecipeRoom()
            }

        }

        binding.btnCancel.setOnClickListener {
            toMainActivity()
        }
    }

    private fun checkInput():Boolean{
        return if (binding.etAuthor.text.isEmpty() || binding.etIngredients.text.isEmpty() || binding.etInstructions.text.isEmpty() || binding.etTitle.text.isEmpty()){
           Toast.makeText(this,"Please Fill all The Fields",Toast.LENGTH_LONG).show()
            false
        }else {
            true
        }
    }

    private fun addRecipe(){
        val apiInterfacePOST = Client().getTheClient()?.create(API::class.java)

        apiInterfacePOST?.addRecipe(
            RecipeesItem(binding.etAuthor.text.toString(),
            binding.etIngredients.text.toString(),
            binding.etInstructions.text.toString(),0,
            binding.etTitle.text.toString())
        )?.enqueue(object : Callback<RecipeesItem> {
            override fun onResponse(call: Call<RecipeesItem>, response: Response<RecipeesItem>) {
                Toast.makeText(this@MainActivityAdd, "Recipe Added successfully",Toast.LENGTH_LONG).show()
                toMainActivity()
            }

            override fun onFailure(call: Call<RecipeesItem>, t: Throwable) {
                Toast.makeText(this@MainActivityAdd, "Something went wrong not able to add Recipe",Toast.LENGTH_LONG).show()
            }

        })

    }
    private fun addRecipeRoom(){
        CoroutineScope(IO).launch {
            val isSave =vm.addRecipesFirebase(Recipe(binding.etAuthor.text.toString(),
                binding.etIngredients.text.toString(),
                binding.etInstructions.text.toString(),"0",
                binding.etTitle.text.toString()))
            withContext(Main){
                if (isSave > 0){
                    Toast.makeText(this@MainActivityAdd, "Recipe Added successfully",Toast.LENGTH_LONG).show()
                    toMainActivity()
                }else{
                    Toast.makeText(this@MainActivityAdd, "Something went wrong not able to add Recipe",Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    fun toMainActivity(){
        val backToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(backToMainActivity)
    }
}