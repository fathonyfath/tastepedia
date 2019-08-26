package id.fathonyfath.tastepedia.data

import android.content.Context
import android.util.Log
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

    suspend fun getAllRecipe(): List<Recipe> = withContext(Dispatchers.IO) {
        val stream = context.assets.open("recipes.json")
        val json = readFileAsString(stream)
        stream.close()
        val recipes = Json.parse(ArrayListSerializer(Recipe.serializer()), json)
        return@withContext recipes.map { it.copy(isFavorite = getRecipeFavoriteStatus(it.id)) }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? = withContext(Dispatchers.IO) {
        val recipes = getAllRecipe()
        return@withContext recipes.find { it.id == recipeId }
    }

    private fun readFileAsString(stream: InputStream, charset: Charset = Charsets.UTF_8): String {
        return stream.bufferedReader(charset).use { it.readText() }
    }

    private fun getRecipeFavoriteStatus(recipeId: Int): Boolean {
        return false
    }
}