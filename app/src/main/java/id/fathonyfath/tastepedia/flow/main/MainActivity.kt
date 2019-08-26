package id.fathonyfath.tastepedia.flow.main

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.fathonyfath.tastepedia.R
import id.fathonyfath.tastepedia.extension.recipeProvider
import id.fathonyfath.tastepedia.flow.CoroutineActivity
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault

@UnstableDefault
class MainActivity : CoroutineActivity() {

    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var recipeProgressBar: ProgressBar

    private lateinit var recipeAdapter: RecipeAdapter

    private var layoutManagerState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.recipesRecyclerView = findViewById(R.id.recipes_recycler_view)
        this.recipeProgressBar = findViewById(R.id.recipes_progress_bar)

        this.recipeAdapter = RecipeAdapter()
        this.recipesRecyclerView.layoutManager = LinearLayoutManager(this)
        this.recipesRecyclerView.adapter = this.recipeAdapter

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
