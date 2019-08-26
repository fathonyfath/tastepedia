package id.fathonyfath.tastepedia

import android.app.Application
import id.fathonyfath.tastepedia.data.RecipeProvider
import kotlinx.serialization.UnstableDefault

@UnstableDefault
class MyApplication : Application() {

    private lateinit var recipeProvider: RecipeProvider

    override fun onCreate() {
        super.onCreate()

        this.recipeProvider = RecipeProvider(this)
    }

    fun recipeProvider(): RecipeProvider = this.recipeProvider
}