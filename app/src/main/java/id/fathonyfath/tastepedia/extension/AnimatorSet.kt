package id.fathonyfath.tastepedia.extension

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.OvershootInterpolator

fun animateScaleAnimatorSet(view: View): AnimatorSet {
    return AnimatorSet().apply {
        val animX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f).apply {
            interpolator = OvershootInterpolator()
        }
        val animY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f).apply {
            interpolator = OvershootInterpolator()
        }

        playTogether(animX, animY)
        start()
    }
}