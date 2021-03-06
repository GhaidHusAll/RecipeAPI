package com.example.recipe_ghh

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe_ghh.api.RecipeesItem
import com.example.recipe_ghh.databinding.RowsBinding
import com.example.recipe_ghh.room.Recipe

class RecipesAdapter(private var list: List<Recipe>, private val activity: Activity):RecyclerView.Adapter<RecipesAdapter.MyHolder>() {
    class MyHolder(val binding: RowsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(RowsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val recipe = list[position]
        holder.binding.apply {
            tvAuthor.text = recipe.author
            tvTitle.text = recipe.title

        }
        holder.binding.cardView.setOnClickListener {
            //display all Information
            (activity as MainActivity).newActivity(position)
        }

    }

    override fun getItemCount() = list.size
    fun update(newList: List<Recipe>){
        this.list = newList
        notifyDataSetChanged()
    }
}

