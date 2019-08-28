package id.fathonyfath.tastepedia.flow.main

import android.animation.AnimatorInflater
import android.animation.StateListAnimator
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.fathonyfath.tastepedia.R
import id.fathonyfath.tastepedia.extension.animateScaleAnimatorSet
import id.fathonyfath.tastepedia.extension.loadImage
import id.fathonyfath.tastepedia.model.Recipe

class RecipeAdapter : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(DIFF_CALLBACK) {

    var onFavoriteClickListener: ((recipe: Recipe, isChecked: Boolean) -> Unit)? = null
    var onItemClickListener: ((recipe: Recipe, pairOfTransition: List<View>) -> Unit)? =
        null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), this.onFavoriteClickListener, this.onItemClickListener)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val context = itemView.context

        private var animator: StateListAnimator? = null

        private val recipeCardView: CardView = itemView.findViewById(R.id.recipe_card_view)
        private val recipeImageView: ImageView = itemView.findViewById(R.id.recipe_image_view)
        private val nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
        private val authorTextView: TextView = itemView.findViewById(R.id.author_text_view)
        private val favoriteCheckBox: CheckBox = itemView.findViewById(R.id.favorite_check_box)
        private val backgroundView: View = itemView.findViewById(R.id.background_view)

        fun bind(
            data: Recipe,
            onFavoriteClickListener: ((recipe: Recipe, isChecked: Boolean) -> Unit)?,
            onItemClickListener: ((recipe: Recipe, pairOfTransition: List<View>) -> Unit)? = null
        ) {
            this.recipeImageView.loadImage(Uri.parse(data.photoUri))
            this.nameTextView.text = data.name
            this.authorTextView.text = context.getString(R.string.subtitle_text, data.author.name)

            this.favoriteCheckBox.setOnClickListener {
                animateScaleAnimatorSet(this.favoriteCheckBox)
            }
            this.favoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onFavoriteClickListener?.invoke(data, isChecked)
            }

            this.recipeCardView.setOnClickListener {
                onItemClickListener?.invoke(
                    data,
                    listOf(
                        this.recipeCardView,
                        this.recipeImageView,
                        this.nameTextView,
                        this.backgroundView
                    )
                )
            }

            ViewCompat.setTransitionName(this.recipeImageView, "recipeImage-${data.id}")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.bindStateListAnimatorToCardView()
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        private fun bindStateListAnimatorToCardView() {
            if (animator == null) {
                animator =
                    AnimatorInflater.loadStateListAnimator(context, R.animator.card_view_elevate)
            }
            this.recipeCardView.stateListAnimator = animator
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }

        }
    }
}