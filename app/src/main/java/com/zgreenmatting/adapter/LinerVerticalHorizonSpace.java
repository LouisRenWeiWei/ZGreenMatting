package com.zgreenmatting.adapter;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 任伟伟
 * Datetime: 2017/2/10-19:45
 * Email: renweiwei@ufashion.com
 */

public class LinerVerticalHorizonSpace extends RecyclerView.ItemDecoration {
    private int topBottom;
    private int leftRight;

    public LinerVerticalHorizonSpace(int topBottom, int leftRight) {
        this.topBottom = topBottom;
        this.leftRight = leftRight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = topBottom;
        outRect.left = leftRight;
    }
}