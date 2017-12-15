package com.cang.zhenpin.zhenpincang.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cang.zhenpin.zhenpincang.R;


/**
 * Created by Allen on 2015/12/15.
 */
public class TopDividerItemDecoration extends RecyclerView.ItemDecoration{

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Paint paint;
    private int paintHeight;

    private Drawable mDivider;

    private int mOrientation;

    public TopDividerItemDecoration(Context context, int orientation) {

        this(context, orientation, 2);
    }

    public TopDividerItemDecoration(Context context, int orientation, int lineHeight) {

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.line));
        paint.setStrokeWidth((float) lineHeight);
        paintHeight = lineHeight;
        setOrientation(orientation);
    }


    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }

    }


    public void drawVertical(Canvas c, RecyclerView parent) {

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int bottom;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();

            if (i == 0) {
                int top0 = child.getTop();
                int bottom0 = top0 + paintHeight;
                c.drawLine(left, top0, right, bottom0, paint);
            }

            final int top = child.getBottom() + params.bottomMargin;
            if (null != mDivider){
                bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }else{
                bottom = top + paintHeight;
                c.drawLine(left, top, right, bottom, paint);
//                c.restore();
            }

        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int right;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;

            if (null != mDivider){
                right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }else{
                right = left + paintHeight;
                c.drawLine(left, top, right, bottom, paint);
                c.restore();
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (mOrientation == VERTICAL_LIST) {

            if (null != mDivider){
                outRect.set(0, 0, 0, view.getMeasuredHeight());
            }else{
                outRect.set(0, 0, 0, paintHeight);
            }
        } else {
            if (null != mDivider){
                outRect.set(0, 0, 0, view.getMeasuredHeight());
            }else{
                outRect.set(0, 0, 0, paintHeight);
            }
        }
    }
}
