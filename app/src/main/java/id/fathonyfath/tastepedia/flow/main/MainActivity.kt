package id.fathonyfath.tastepedia.flow.main

import android.animation.AnimatorInflater
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.fathonyfath.tastepedia.R
import id.fathonyfath.tastepedia.extension.PaddingItemDecoration
import id.fathonyfath.tastepedia.extension.recipeProvider
import id.fathonyfath.tastepedia.flow.CoroutineActivity
import id.fathonyfath.tastepedia.flow.detail.DetailActivity
import id.fathonyfath.tastepedia.model.Recipe
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault

@UnstableDefault
class MainActivity : CoroutineActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var recipeProgressBar: ProgressBar

    private lateinit var recipeAdapter: RecipeAdapter

    private var lastSavedCanScrollVertically = false

    private var layoutManagerState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Intent(this, DetailActivity::class.java).apply {
            startActivity(this)
        }

        this.toolbar = findViewById(R.id.toolbar)
        this.recipesRecyclerView = findViewById(R.id.recipes_recycler_view)
        this.recipeProgressBar = findViewById(R.id.recipes_progress_bar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setupToolbarStateList()
        }

        this.recipesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val canScrollVertically = recyclerView.canScrollVertically(-1)
                if (canScrollVertically != this@MainActivity.lastSavedCanScrollVertically) {
                    this@MainActivity.lastSavedCanScrollVertically = canScrollVertically
                    this@MainActivity.toolbar.isSelected = canScrollVertically
                }
            }
        })

        val itemDecoration = PaddingItemDecoration(this, 16)
        this.recipesRecyclerView.addItemDecoration(itemDecoration)

        this.recipeAdapter = RecipeAdapter()
        this.recipesRecyclerView.layoutManager = LinearLayoutManager(this)
        this.recipesRecyclerView.adapter = this.recipeAdapter

        this.recipeAdapter.onFavoriteClickListener = { recipe, isChecked ->
            Toast.makeText(
                this,
                "Recipe: $recipe is ${if (isChecked) "favorited" else "not favorited"}.",
                Toast.LENGTH_SHORT
            ).show()
        }

        this.recipeAdapter.onItemClickListener = { recipe, sharedImageView ->
            this.navigateToDetail(recipe, sharedImageView)
        }

        uiScope.launch {
            updateAdapterItem()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        this.recipesRecyclerView.layoutManager?.let { layoutManager ->
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupToolbarStateList() {
        val animator = AnimatorInflater.loadStateListAnimator(this, R.animator.toolbar_elevate)
        this.toolbar.stateListAnimator = animator
    }

    private fun navigateToDetail(recipe: Recipe, sharedElement: ImageView) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.RECIPE_ID, recipe.id)
            putExtra(
                DetailActivity.IMAGE_TRANSITION_NAME,
                ViewCompat.getTransitionName(sharedElement)
            )
        }

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            sharedElement, recipe.id.toString()
        )

        ActivityCompat.startActivity(this, intent, options.toBundle())
    }

    private suspend fun updateAdapterItem() {
        this.recipeProgressBar.visibility = View.VISIBLE
        this.recipesRecyclerView.visibility = View.GONE

        val result = recipeProvider().getAllRecipe()
        this.recipeAdapter.submitList(result)

        this.recipeProgressBar.visibility = View.GONE
        this.recipesRecyclerView.visibility = View.VISIBLE

        this.layoutManagerState?.let { savedState ->
            this.recipesRecyclerView.layoutManager?.onRestoreInstanceState(savedState)
        }
    }

    companion object {
        const val LAYOUT_MANAGER_STATE = "LayoutMangerStateKey"
    }
}
