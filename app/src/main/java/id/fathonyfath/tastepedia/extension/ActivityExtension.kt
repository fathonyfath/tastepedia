package id.fathonyfath.tastepedia.extension

import android.app.Activity
import id.fathonyfath.tastepedia.MyApplication
import id.fathonyfath.tastepedia.data.RecipeProvider
import kotlinx.serialization.UnstableDefault


@UnstableDefault
private fun Activity.myApplication(): MyApplication = this.application as MyApplication

@UnstableDefault
fun Activity.recipeProvider(): RecipeProvider = this.myApplication().recipeProvider()
