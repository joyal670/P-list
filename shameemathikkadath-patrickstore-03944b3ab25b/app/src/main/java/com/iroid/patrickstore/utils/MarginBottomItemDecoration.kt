package com.iroid.patrickstore.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginBottomItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount-1
        if (itemPosition !=itemCount) {
            if(itemPosition ==2){
                with(outRect) {
                    spaceHeight
                    right = 0
                }
            }else{
                with(outRect) {
                    spaceHeight
                    right = spaceHeight
                }
            }
        }else{
            with(outRect) {
                spaceHeight
                right = 0
            }
        }
    }
}