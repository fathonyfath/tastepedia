package id.fathonyfath.tastepedia

import android.app.Activity
import android.app.Application
import android.util.Log
import id.fathonyfath.tastepedia.data.RecipeProvider
import kotlinx.serialization.UnstableDefault

@UnstableDefault
class MyApplication : Application() {

    private lateinit var recipeProvider: RecipeProvider

    override fun onCreate() {
        super.onCreate()

        this.recipeProvider = RecipeProvider(this)
        Log.d("MyApplication", "List: ${this.recipeProvider.getAllRecipe()}")
    }

    fun recipeProvider(): RecipeProvider = this.recipeProvider
}

@UnstableDefault
private fun Activity.myApplication(): MyApplication = this.application as MyApplication

@UnstableDefault
private fun Activity.recipeProvider(): RecipeProvider = this.myApplication().recipeProvider()