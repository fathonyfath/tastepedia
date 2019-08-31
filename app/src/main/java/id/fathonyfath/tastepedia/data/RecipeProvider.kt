package id.fathonyfath.tastepedia.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import id.fathonyfath.tastepedia.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.InputStream
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.json.Json
import java.nio.charset.Charset

@UnstableDefault
class RecipeProvider(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    suspend fun getAllRecipe(): List<Recipe> = withContext(Dispatchers.IO) {
        val stream = context.assets.open("recipes.json")
        val json = readFileAsString(stream)
        stream.close()
        val recipes = Json.parse(ArrayListSerializer(Recipe.serializer()), json)
        return@withContext recipes.map {
            it.copy().apply { isFavorite = getRecipeFavoriteStatus(it.id) }
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? = withContext(Dispatchers.IO) {
        val recipes = getAllRecipe()
        return@withContext recipes.find { it.id == recipeId }
    }

    suspend fun updateRecipeFavorite(recipeId: Int, isFavorite: Boolean) =
        withContext(Dispatchers.IO) {
            sharedPreferences.edit(commit = true) {
                putBoolean("RECIPE-$recipeId", isFavorite)
            }
        }

    private fun readFileAsString(stream: InputStream, charset: Charset = Charsets.UTF_8): String {
        return stream.bufferedReader(charset).use { it.readText() }
    }

    private fun getRecipeFavoriteStatus(recipeId: Int): Boolean {
        return this.sharedPreferences.getBoolean("RECIPE-$recipeId", false)
    }
}