package com.ahmedmatem.lib.mathkeyboard.ui;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import com.ahmedmatem.lib.mathkeyboard.contracts.Display;
import com.ahmedmatem.lib.mathkeyboard.util.DisplayContent;

public class DisplayView extends WebView implements Display {
    public DisplayView(Context context) {
        super(context);
    }

    public DisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DisplayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void print(DisplayContent content) {
        loadUrl("javascript: showContent(\"" + content.toString() + "\", " + content.cursorPosition + ")");
    }
}
