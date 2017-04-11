package br.com.brasolia.util;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by cayke on 11/04/17.
 */

public class RectangularFrameLayout extends FrameLayout{
    static double proportion = 0.49;

    public RectangularFrameLayout(@NonNull Context context) {
        super(context);
    }

    public RectangularFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangularFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int  heightSpec = (int) (widthMeasureSpec*proportion);
        super.onMeasure(widthMeasureSpec, heightSpec);

        int width = getMeasuredWidth();
        int height = (int) (width*proportion);
        setMeasuredDimension(width,height);
    }
}
