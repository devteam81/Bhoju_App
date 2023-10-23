package com.bhoju.app.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import com.bhoju.app.R;


public class DrawableUtils {

    private DrawableUtils() {

    }

    public static void changeIconDrawableToGray(Context context, Drawable drawable) {
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(ContextCompat
                    .getColor(context, R.color.dark_gray), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
