package id.fathonyfath.tastepedia.extension

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import id.fathonyfath.tastepedia.module.GlideApp
import id.fathonyfath.tastepedia.module.GlideRequest

fun ImageView.loadImage(
    fromUri: Uri,
    applier: (GlideRequest<Drawable>.() -> GlideRequest<Drawable>)? = null,
    onComplete: (() -> Unit)? = null
) {
    GlideApp.with(this)
        .load(fromUri)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .run {
            if (applier != null) {
                return@run applier.invoke(this)
            } else {
                return@run this
            }
        }
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onComplete?.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onComplete?.invoke()
                return false
            }

        })
        .into(this)
}