package com.ahmedmatem.lib.mathkeyboard.util.keys;

import com.ahmedmatem.lib.mathkeyboard.contracts.KeyType;

import java.util.List;

public class FuncKey extends PrintableKey {
    public FuncKey(int unicode, int resId, List<? extends Key> children) {
        super(unicode, resId, children);
    }

    public FuncKey(char keyChar, List<? extends Key> children) {
        super(keyChar, children);
    }

    @Override
    public int getType() {
        return KeyType.FUNCTIONAL;
    }
}
