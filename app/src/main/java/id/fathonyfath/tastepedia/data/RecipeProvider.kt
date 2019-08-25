package id.fathonyfath.tastepedia.data

import id.fathonyfath.tastepedia.model.Recipe
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

@UnstableDefault
class RecipeProvider {
    fun getAllRecipe(): List<Recipe> {
        val recipe: Recipe = Json.parse(Recipe.serializer(), "")
        return emptyList()
    }

    fun getRecipeByID(recipeID: Int): Recipe? {
        return null
    }
}