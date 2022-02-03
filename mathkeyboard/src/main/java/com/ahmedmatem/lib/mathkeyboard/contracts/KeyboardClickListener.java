package com.ahmedmatem.lib.mathkeyboard.contracts;

import android.view.View;

import com.ahmedmatem.lib.mathkeyboard.util.keys.Key;
import com.ahmedmatem.lib.mathkeyboard.util.keys.NavKey;
import com.ahmedmatem.lib.mathkeyboard.util.keys.PrintableKey;

public interface KeyboardClickListener {
    void onNavKeyClick(NavKey key);
    void onPrintableKeyClick(PrintableKey key);
    <T extends Key> void onKeyLongPress(View v, T key);
    void onSubmit(Key key);
}
