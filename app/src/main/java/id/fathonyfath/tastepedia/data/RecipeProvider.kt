package id.fathonyfath.tastepedia.data

import android.content.Context
import id.fathonyfath.tastepedia.model.Recipe
import kotlinx.io.InputStream
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.json.Json
import java.nio.charset.Charset

@UnstableDefault
class RecipeProvider(private val context: Context) {

    fun getAllRecipe(): List<Recipe> {
        val stream = context.assets.open("recipes.json")
        val json = readFileAsString(stream)
        stream.close()
        val recipes = Json.parse(ArrayListSerializer(Recipe.serializer()), json)
        return recipes.map { it.copy(isFavorite = getRecipeFavoriteStatus(it.id)) }
    }

    fun getRecipeByID(recipeId: Int): Recipe? {
        val recipes = getAllRecipe()
        return recipes.find { it.id == recipeId }
    }

    private fun readFileAsString(stream: InputStream, charset: Charset = Charsets.UTF_8): String {
        return stream.bufferedReader(charset).use { it.readText() }
    }

    private fun getRecipeFavoriteStatus(recipeId: Int): Boolean {
        return false
    }
}