package com.ashtechlabs.teleporter.item_deceration;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
        outRect.top = verticalSpaceHeight;
        outRect.right = verticalSpaceHeight;
        outRect.left = verticalSpaceHeight;


//        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
//            outRect.bottom = verticalSpaceHeight;
//        }
    }
}