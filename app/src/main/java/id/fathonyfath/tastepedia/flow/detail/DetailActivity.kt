package id.fathonyfath.tastepedia.flow.detail

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.CollapsingToolbarLayout
import id.fathonyfath.tastepedia.R
import id.fathonyfath.tastepedia.extension.loadImage
import id.fathonyfath.tastepedia.extension.recipeProvider
import id.fathonyfath.tastepedia.flow.CoroutineActivity
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault

@UnstableDefault
class DetailActivity : CoroutineActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var recipeImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        this.toolbar = findViewById(R.id.toolbar)
        this.collapsingToolbar = findViewById(R.id.toolbar_collapsing)
        this.recipeImageView = findViewById(R.id.recipe_image_view)

        this.collapsingToolbar.post { this.collapsingToolbar.requestLayout() }

        getImageTransitionNameArgs()?.let { transitionName ->
            ViewCompat.setTransitionName(this.recipeImageView, transitionName)
        }

        this.toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        uiScope.launch {
            getRecipeDetail()
        }
    }

    private suspend fun getRecipeDetail() {
        val id = getRecipeIdArgs()
        if (id == null) {
            finish()
            return
        }
        val recipe = recipeProvider().getRecipeById(id)
        if (recipe == null) {
            finish()
            return
        }

        this.recipeImageView.loadImage(Uri.parse(recipe.photoUri))
    }

    private fun getRecipeIdArgs(): Int? {
        val id = intent.getIntExtra(RECIPE_ID, -1)
        if (id == -1) {
            return null
        }
        return id
    }

    private fun getImageTransitionNameArgs(): String? {
        val name = intent.getStringExtra(IMAGE_TRANSITION_NAME)
        if (name.isNullOrBlank()) {
            return null
        }
        return name
    }

    companion object {
        const val RECIPE_ID = "RecipeId"
        const val IMAGE_TRANSITION_NAME = "ImageTransitionName"
    }
}