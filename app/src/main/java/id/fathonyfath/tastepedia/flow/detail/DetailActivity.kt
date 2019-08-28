package id.fathonyfath.tastepedia.flow.detail

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var detailBackgroundView: View
    private lateinit var detailRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportPostponeEnterTransition()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.sharedElementEnterTransition = ChangeBounds().apply {
                duration = 200
            }
            window.sharedElementReturnTransition = ChangeBounds().apply {
                duration = 200
            }
        }

        this.toolbar = findViewById(R.id.toolbar)
        this.collapsingToolbar = findViewById(R.id.toolbar_collapsing)
        this.recipeImageView = findViewById(R.id.recipe_image_view)
        this.detailBackgroundView = findViewById(R.id.detail_background)

        this.collapsingToolbar.post { this.collapsingToolbar.requestLayout() }

        this.toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        recipeIdArgs?.let { recipeId ->
            ViewCompat.setTransitionName(recipeImageView, "recipeImage-$recipeId")
        }

        uiScope.launch {
            getRecipeDetail()
        }
    }

    private suspend fun getRecipeDetail() {
        val id = recipeIdArgs
        if (id == null) {
            finish()
            return
        }
        val recipe = recipeProvider().getRecipeById(id)
        if (recipe == null) {
            finish()
            return
        }

        this.collapsingToolbar.title = recipe.name
        this.recipeImageView.loadImage(Uri.parse(recipe.photoUri)) {
            supportStartPostponedEnterTransition()
        }
    }

    private val recipeIdArgs: Int?
        get() {
            val id = intent.getIntExtra(RECIPE_ID, -1)
            if (id == -1) {
                return null
            }
            return id
        }

    companion object {
        const val RECIPE_ID = "RecipeId"
    }
}