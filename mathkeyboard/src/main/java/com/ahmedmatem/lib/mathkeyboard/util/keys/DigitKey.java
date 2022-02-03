package com.ahmedmatem.lib.mathkeyboard.util.keys;

import com.ahmedmatem.lib.mathkeyboard.contracts.KeyType;

import java.util.List;

public class DigitKey extends PrintableKey {

    public DigitKey(char digit, List<? extends Key> children) {

        super(digit, children);
    }

    @Override
    public int getType() {
        return KeyType.DIGIT;
    }
}
