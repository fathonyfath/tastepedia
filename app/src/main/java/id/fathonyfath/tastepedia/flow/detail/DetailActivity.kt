package id.fathonyfath.tastepedia.flow.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.transition.ChangeBounds
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import id.fathonyfath.tastepedia.R
import id.fathonyfath.tastepedia.extension.loadImage
import id.fathonyfath.tastepedia.extension.recipeProvider
import id.fathonyfath.tastepedia.flow.CoroutineActivity
import id.fathonyfath.tastepedia.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault

@UnstableDefault
class DetailActivity : CoroutineActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var recipeImageView: ImageView
    private lateinit var detailBackgroundView: View
    private lateinit var detailProgressBar: ProgressBar
    private lateinit var detailRecycler: RecyclerView

    private lateinit var detailItemAdapter: DetailItemAdapter

    private var layoutManagerState: Parcelable? = null

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
        this.detailProgressBar = findViewById(R.id.detail_progress_bar)
        this.detailRecycler = findViewById(R.id.detail_recycler_view)

        this.detailRecycler.layoutManager = LinearLayoutManager(this)

        this.detailItemAdapter = DetailItemAdapter()
        this.detailItemAdapter.onUriContentClickListener = { uri ->
            Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
                startActivity(this)
            }
        }

        this.detailRecycler.adapter = this.detailItemAdapter


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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        this.detailRecycler.layoutManager?.let { layoutManager ->
            outState.putParcelable(LAYOUT_MANAGER_STATE, layoutManager.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState?.getParcelable<Parcelable>(LAYOUT_MANAGER_STATE)
            ?.let { layoutManagerState ->
                this.layoutManagerState = layoutManagerState
            }
    }

    private suspend fun getRecipeDetail() {
        this.detailProgressBar.visibility = View.VISIBLE
        this.detailRecycler.visibility = View.GONE

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

        val list = composeDetailItem(recipe)

        this.detailItemAdapter.submitList(list)

        this.detailProgressBar.visibility = View.GONE
        this.detailRecycler.visibility = View.VISIBLE

        this.layoutManagerState?.let { savedState ->
            this.detailRecycler.layoutManager?.onRestoreInstanceState(savedState)
        }
    }

    private suspend fun composeDetailItem(recipe: Recipe): List<DetailItemType> =
        withContext(Dispatchers.Default) {
            val ingredientsTitle = DetailItemType.Header(getString(R.string.ingredients_text))
            val ingredients = recipe.ingredients.map { DetailItemType.Content(it) }
            val cookingStepsTitle = DetailItemType.Header(getString(R.string.cooking_steps_text))
            val cookingSteps = recipe.cookingSteps.map { DetailItemType.Content(it) }
            val author = listOf(
                DetailItemType.Header(getString(R.string.author_text)),
                DetailItemType.Content(recipe.author.name),
                DetailItemType.UriContent(recipe.author.originalUri)
            )

            return@withContext listOf(ingredientsTitle) + ingredients + listOf(cookingStepsTitle) +
                    cookingSteps + author
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
        const val LAYOUT_MANAGER_STATE = "LayoutMangerStateKey"
    }
}