package id.fathonyfath.tastepedia.extension

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class PaddingItemDecoration(private val context: Context, paddingInDp: Int) :
    RecyclerView.ItemDecoration() {

    private var paddingInPx: Int

    var paddingInDp: Int = paddingInDp
        set(value) {
            field = value
            this.paddingInPx = getPaddingAsPx()
        }

    init {
        this.paddingInPx = getPaddingAsPx()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.bottom = this.paddingInPx
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = getPaddingAsPx()
        }

        outRect.left = this.paddingInPx
        outRect.right = this.paddingInPx
    }

    private fun getPaddingAsPx(): Int {
        return (paddingInDp * context.resources.displayMetrics.density).roundToInt()
    }
}