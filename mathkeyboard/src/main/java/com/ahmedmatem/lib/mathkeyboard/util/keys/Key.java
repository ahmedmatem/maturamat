package com.ahmedmatem.lib.mathkeyboard.util.keys;

import java.util.ArrayList;
import java.util.List;

public abstract class Key {
    private int drawableResourceId;
    private char keyChar;
    private int unicode;

    public boolean hasChildren;
    public boolean isImageKey;
    public List<? extends Key> children = new ArrayList<Key>();

    /**
     * Use in case of image key
     */
    public Key(int unicode, int drawableResourceId, List<? extends Key> children){
        this.unicode = unicode;
        isImageKey = true;
        this.drawableResourceId = drawableResourceId;
        if (children != null) {
            hasChildren = true;
            this.children = children;
        }
    }

    /**
     * Use in case of Text key
     */
    public Key(char keyChar, List<? extends Key> children){
        this.keyChar = keyChar;
        this.unicode = (int) keyChar;
        if (children != null) {
            hasChildren = true;
            this.children = children;
        }
    }

    public int getDrawable(){
        return drawableResourceId;
    }

    public int getUnicode(){
        return unicode;
    }

    public abstract int getType();
}
