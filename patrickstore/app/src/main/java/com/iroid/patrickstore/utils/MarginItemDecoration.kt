package com.iroid.patrickstore.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val isLast: Int = state.itemCount - 1
        if (position != isLast) {
            with(outRect) {
                right = spaceHeight
            }
        } else {
            with(outRect) {
                right = 0
            }
        }
    }
}