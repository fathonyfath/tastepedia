package id.fathonyfath.tastepedia.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Int,
    val photoUri: String,
    val name: String,
    val ingredients: List<String>,
    val cookingSteps: List<String>,
    val author: Author
) {
    var isFavorite: Boolean = false
}