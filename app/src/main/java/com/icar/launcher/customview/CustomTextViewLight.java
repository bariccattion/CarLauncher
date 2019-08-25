package com.icar.launcher.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextViewLight extends androidx.appcompat.widget.AppCompatTextView {
    public CustomTextViewLight(Context context) {
        super(context);
        setFont();
    }

    public CustomTextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextViewLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface fontMedium = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoogleSans-Medium.ttf");
        setTypeface(fontMedium, Typeface.NORMAL);
    }
}