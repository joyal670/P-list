package com.iroid.patrickstore.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginGridDecoration2(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount - 1
        if (itemPosition % 2 == 0) {
            with(outRect) {
                spaceHeight
                right = spaceHeight
            }
        } else {
            with(outRect) {
                spaceHeight
                right = 0
            }
        }
    }
}