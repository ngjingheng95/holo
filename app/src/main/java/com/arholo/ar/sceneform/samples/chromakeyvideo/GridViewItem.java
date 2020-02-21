package com.arholo.ar.sceneform.samples.chromakeyvideo;

import android.content.Context;
import android.util.AttributeSet;

// Makes Pixabay thumbnails loaded into ImageView to be square
public class GridViewItem extends android.support.v7.widget.AppCompatImageView {

    public GridViewItem(Context context) {
        super(context);
    }

    public GridViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
