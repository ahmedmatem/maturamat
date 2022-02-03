package com.ahmedmatem.lib.mathkeyboard.util.keys;

import com.ahmedmatem.lib.mathkeyboard.contracts.KeyType;

import java.util.List;

public class NavKey extends Key {
    public NavKey(int unicode, int resId, List<? extends Key> children) {
        super(unicode, resId, children);
    }

    public NavKey(char keyChar, List<? extends Key> children) {
        super(keyChar, children);
    }

    @Override
    public int getType() {
        return KeyType.NAVIGATION;
    }
}
