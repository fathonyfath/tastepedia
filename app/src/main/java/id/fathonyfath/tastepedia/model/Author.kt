package id.fathonyfath.tastepedia.model

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val name: String,
    val originalUri: String
)