package com.icar.launcher.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    public CustomTextView(Context context) {
        super(context);
        setFont();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoogleSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
        Typeface fontMedium = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoogleSans-Medium.ttf");
        setTypeface(fontMedium, Typeface.BOLD);
    }
}