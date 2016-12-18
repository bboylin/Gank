package com.bboylin.gank.UI.Widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by h on 15/11/13.
 */
public class SimpleItemDecoration extends RecyclerView.ItemDecoration {
    Paint paint = new Paint();

    private float MARGIN = 48;

    public SimpleItemDecoration(float margin) {
        this.MARGIN = margin;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        paint.setARGB(100, 20, 20, 20);
        for (int i = 0, size = parent.getChildCount(); i < size; i++) {
            View child = parent.getChildAt(i);
            c.drawLine(child.getLeft() + MARGIN, child.getBottom(), child.getRight() - MARGIN, child.getBottom(), paint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

}
