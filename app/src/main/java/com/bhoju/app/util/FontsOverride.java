package com.bhoju.app.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.bhoju.app.util.download.Downloader;

import java.lang.reflect.Field;

public final class FontsOverride {

    private static final String TAG = FontsOverride.class.getSimpleName();

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        } catch (IllegalAccessException e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
    }
}
