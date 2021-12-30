package com.example.recipe_ghh.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Recipe::class],version = 1,exportSchema = false)
abstract class DatabaseApp : RoomDatabase() {
    abstract fun myDao(): RecipeDao

    companion object{
        @Volatile
        private var theDBobject: DatabaseApp? =null

        fun getDB(context:Context): DatabaseApp{
            val temp = theDBobject
            if (temp != null){
                return temp
            }
            synchronized(this){
                val newDBobject = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseApp::class.java,
                    "recipe"
                ).fallbackToDestructiveMigration().build()
                theDBobject = newDBobject
                return newDBobject
            }
        }
    }
}