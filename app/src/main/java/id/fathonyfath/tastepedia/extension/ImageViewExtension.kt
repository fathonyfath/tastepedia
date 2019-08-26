package id.fathonyfath.tastepedia.extension

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import id.fathonyfath.tastepedia.module.GlideApp

fun ImageView.loadImage(fromUri: Uri) {
    GlideApp.with(this)
        .load(fromUri)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}