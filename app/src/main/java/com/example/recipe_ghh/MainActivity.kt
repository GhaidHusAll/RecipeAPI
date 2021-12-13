package com.example.recipe_ghh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe_ghh.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var recipeList : ArrayList<RecipeesItem>
    public var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.fABtnAdd.setOnClickListener{
            val toAddActivity = Intent(this, MainActivityAdd::class.java)
            startActivity(toAddActivity)
        }
        getRecipes()
    }

    private fun getRecipes(){
        recipeList = arrayListOf()
        val apiGET = Client().getTheClient()?.create(API::class.java)
        apiGET?.getRecipe()?.enqueue(object: Callback<Recipes> {
            override fun onResponse(call: Call<Recipes>, response: Response<Recipes>) {
                try {
                    recipeList = response.body()!!
                    setMainRVAdapter()
                    Log.d("MAIN", "ISSUE: successfully")

                }catch (e:Exception){
                    Log.d("MAIN", "ISSUE: $e")
                }
            }

            override fun onFailure(call: Call<Recipes>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }
    fun setMainRVAdapter(){
        binding.mainRV.adapter = RecipesAdapter(recipeList,this)
        binding.mainRV.layoutManager = LinearLayoutManager(this)
        binding.mainRV.setHasFixedSize(true)
    }
    fun newActivity(recipeIndex: Int){
        val toDisplayActivity = Intent(this, MainActivityDisplay::class.java)
        toDisplayActivity.putExtra("recipeTitle" , recipeList[recipeIndex].title)
        toDisplayActivity.putExtra("recipeAuthor" , recipeList[recipeIndex].author)
        toDisplayActivity.putExtra("recipeIng" , recipeList[recipeIndex].ingredients)
        toDisplayActivity.putExtra("recipeIns" , recipeList[recipeIndex].instructions)

        startActivity(toDisplayActivity)
    }
}