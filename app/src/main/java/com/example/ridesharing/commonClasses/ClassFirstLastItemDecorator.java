package com.example.ridesharing.commonClasses;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Класс, отвечающий за декорацию первого и последнего элемента RecyclerView
 */
public class ClassFirstLastItemDecorator extends RecyclerView.ItemDecoration {
    private int padding;

    public ClassFirstLastItemDecorator(int padding) {
        this.padding = padding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int lastPosition = state.getItemCount() - 1;

        if (position == 0 && !(position == lastPosition)) {
            outRect.top = padding;
        } else if (position == lastPosition) {
            outRect.bottom = padding;
        }
    }
}