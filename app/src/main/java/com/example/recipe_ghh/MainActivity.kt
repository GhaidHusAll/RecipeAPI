package com.example.recipe_ghh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe_ghh.ViewModel.RecipeViewModel
import com.example.recipe_ghh.api.API
import com.example.recipe_ghh.api.Client
import com.example.recipe_ghh.api.Recipes
import com.example.recipe_ghh.databinding.ActivityMainBinding
import com.example.recipe_ghh.room.DatabaseApp
import com.example.recipe_ghh.room.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var recipeList : List<Recipe>
    private lateinit var myAdapter: RecipesAdapter
    private val dao by lazy { DatabaseApp.getDB(this).myDao() }
    private val vm by lazy { ViewModelProvider(this).get(RecipeViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setMainRVAdapter()

        binding.fABtnAdd.setOnClickListener{
            val toAddActivity = Intent(this, MainActivityAdd::class.java)
            startActivity(toAddActivity)
        }
       // getRecipes()
       // gerRecipesRoom()

        vm.getRecipesFirebase().observe(this,{
            recipes ->
            recipeList = recipes
            myAdapter.update(recipeList)
        })
    }

    private fun getRecipes(){
//        recipeList = arrayListOf()
        val apiGET = Client().getTheClient()?.create(API::class.java)
        apiGET?.getRecipe()?.enqueue(object: Callback<Recipes> {
            override fun onResponse(call: Call<Recipes>, response: Response<Recipes>) {
                try {
                  //  recipeList = response.body()!!
                  //  setMainRVAdapter()
                    Log.d("MAIN", "ISSUE: successfully")

                }catch (e:Exception){
                    Log.d("MAIN", "ISSUE: $e")
                }
            }

            override fun onFailure(call: Call<Recipes>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Something went wrong not able to fetch Recipes",
                    Toast.LENGTH_LONG).show()
            }


        })
    }
    private fun gerRecipesRoom(){
        CoroutineScope(IO).launch {
            val recipes = async { dao.getRecipes() }.await()
            if (recipes.isNotEmpty()){
                recipeList = recipes as ArrayList<Recipe>
                withContext(Main){
                }
            }else{
                Log.d("MAIN", "Not able to get data")
            }
        }
    }
    private fun setMainRVAdapter(){
        recipeList = listOf()
        myAdapter= RecipesAdapter(recipeList,this)
        binding.mainRV.adapter = myAdapter
        binding.mainRV.layoutManager = LinearLayoutManager(this)
        binding.mainRV.setHasFixedSize(true)
    }
    fun newActivity(recipeIndex: Int){
        val toDisplayActivity = Intent(this, MainActivityDisplay::class.java)
        toDisplayActivity.putExtra("recipeTitle" , recipeList[recipeIndex].title)
        toDisplayActivity.putExtra("recipeAuthor" , recipeList[recipeIndex].author)
        toDisplayActivity.putExtra("recipeIng" , recipeList[recipeIndex].ingredients)
        toDisplayActivity.putExtra("recipeIns" , recipeList[recipeIndex].instructions)
        toDisplayActivity.putExtra("recipeId" , recipeList[recipeIndex].pk)


        startActivity(toDisplayActivity)
    }
}