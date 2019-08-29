package id.fathonyfath.tastepedia.extension

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.CompoundButtonCompat

class CenteredCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.checkboxStyle
) : AppCompatCheckBox(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        val buttonDrawable = CompoundButtonCompat.getButtonDrawable(this)
        if (buttonDrawable != null) {
            val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK
            val horizontalGravity = gravity and Gravity.HORIZONTAL_GRAVITY_MASK

            val drawableHeight = buttonDrawable.intrinsicHeight
            val drawableWidth = buttonDrawable.intrinsicWidth

            val top = when (verticalGravity) {
                Gravity.BOTTOM -> height - drawableHeight
                Gravity.CENTER_VERTICAL -> (height - drawableHeight) / 2
                else -> 0
            }

            val bottom = top + drawableHeight

            val left = when {
                horizontalGravity == Gravity.CENTER_HORIZONTAL -> (width - drawableWidth) / 2
                isLayoutRtl() -> width - drawableWidth
                else -> 0
            }

            val right = when {
                horizontalGravity == Gravity.CENTER_HORIZONTAL -> left + drawableWidth
                isLayoutRtl() -> width
                else -> drawableWidth
            }

            buttonDrawable.setBounds(left, top, right, bottom)

            val background = background
            DrawableCompat.setHotspotBounds(background, left, top, right, bottom)
        }

        if (buttonDrawable != null) {
            val scrollX = this.scrollX
            val scrollY = this.scrollX
            if (scrollX == 0 && scrollY == 0) {
                buttonDrawable.draw(canvas)
            } else {
                canvas.translate(scrollX.toFloat(), scrollY.toFloat())
                buttonDrawable.draw(canvas)
                canvas.translate((-scrollX).toFloat(), (-scrollY).toFloat())
            }
        }
    }

    private fun isLayoutRtl(): Boolean {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
    }
}