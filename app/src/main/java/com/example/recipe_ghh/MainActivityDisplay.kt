package com.example.recipe_ghh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recipe_ghh.databinding.ActivityMainDisplayBinding

class MainActivityDisplay : AppCompatActivity() {
    private lateinit var binding : ActivityMainDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayInformation()
        binding.btnBack.setOnClickListener {
            val backToMain = Intent(this,MainActivity::class.java)
            startActivity(backToMain)
        }
    }

    private fun displayInformation(){
        val recipeTitle = intent.getStringExtra("recipeTitle")
        val recipeAuthor = intent.getStringExtra("recipeAuthor")
        val recipeIng = intent.getStringExtra("recipeIng")
        val recipeIns = intent.getStringExtra("recipeIns")

        binding.tvTitle.text = recipeTitle
        binding.tvAuthor.text = recipeAuthor
        binding.tvIngredients.text = recipeIng
        binding.tvInstructions.text = recipeIns
    }
}