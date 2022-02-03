package com.ahmedmatem.lib.mathkeyboard.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ahmedmatem.lib.mathkeyboard.config.Constants;

public class DisplayContent {
    public StringBuilder content;
    public int cursorPosition;

    public DisplayContent(){
        init();
    }

    public void setContent(String content){
        Log.d("TEST", "setContent: <" + content + ">");
        if(content == null ||  content.isEmpty()) {
            init();
        } else {
            this.content = new StringBuilder(content);
            cursorPosition = content.length() - 1;
        }
    }

    private void init(){
        content = new StringBuilder("$$");
        cursorPosition = 1;
    }

    // todo: restrict display content length (for example 100 characters)

    public void insert(String str, int cursorOffset) {
        // remove emptyChar in '{?}' when inserting new str
        if(hasEmptyCharNextToCursor()){
            if((content.charAt(cursorPosition - 1) + "").equals(Constants.EMPTY_CHAR)){
                // in case of cursor after empty char
                moveCursorLeft();
            }
            // remove empty char at cursorPosition
            content = content.deleteCharAt(cursorPosition);
        }

        // ' ' (white space) prevention
        if(str.equals(" ")){
            if(cursorPosition == 1){ // prevent start with space
                return;
            }
            // prevent inserting two spaces - '$ $$ $' - one after another
            if ((content.charAt(cursorPosition - 1) + "").equals("$") ||
                ((content.charAt(cursorPosition) + "").equals("$") && cursorPosition != content.length() - 1)) {
                return;
            }
            str = Constants.SPACE;
        }

        // insert str
        content.insert(cursorPosition, str);
        // move cursor
        if(str.equals("$ $")){
            cursorPosition += 2;
        }
        cursorPosition += cursorOffset;
    }

    public void insert(String str){
        this.insert(str, 1);
    }

    public void clear(){
        content = new StringBuilder("$$");
        cursorPosition = 1;
    }

    public void moveCursorLeft(){
        this.moveCursorLeft(1);
    }

    public void moveCursorLeft(int offset){
        if(cursorPosition > offset){
            cursorPosition -= offset;
        }
        // validate cursor position
        if(cursorPosition > 1){
            if(Constants.CURSOR_JUMP_CHARS.contains(content.charAt(cursorPosition))){
                if((content.charAt(cursorPosition) + "").equals("$")){
                    // in case of space - '$ $'
                    cursorPosition -= 2;
                } else if(Constants.CURSOR_JUMP_CHARS.contains(content.charAt(cursorPosition - 1))){
                    moveCursorLeft();
                }
            }
        }
    }

    public void moveCursorRight(){
        this.moveCursorRight(1);
    }

    public void moveCursorRight(int offset){
        if(cursorPosition < content.length() - offset){
            cursorPosition += offset;
        }
        // validate cursor position
        if(cursorPosition < content.length() - 1){
            if(Constants.CURSOR_JUMP_CHARS.contains(content.charAt(cursorPosition - 1))){
                if((content.charAt(cursorPosition - 1) + "").equals("$")){
                    // in case of space - '$ $'
                    cursorPosition += 2;
                } else if(Constants.CURSOR_JUMP_CHARS.contains(content.charAt(cursorPosition))){
                    moveCursorRight();
                }
            }
        }
    }

    // backspace
    public void deleteBack() {
        if(cursorPosition > 1){
            // check for space - '$ $'
            if (cursorPosition > 3 &&
                    content.substring(cursorPosition - 3, cursorPosition).equals("$ $")) {
                // delete space - '$ $'
                content = content.delete(cursorPosition - 3, cursorPosition);
                cursorPosition -= 3;
                return;
            }

            moveCursorLeft();

            if (!((content.charAt(cursorPosition) + "").equals("{") ||
                (content.charAt(cursorPosition) + "").equals("}"))) {
                // delete char at cursorPosition
                content = content.deleteCharAt(cursorPosition);
            } else {
                deleteBack();
            }
        }
    }

    /**
     * Prepare content for display
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        // Manipulating content before insert cursor.
        // In case of content end with space and cursor is before it: '....|$ $$'
        if((cursorPosition + Constants.SPACE.length()) == content.length() - 1 &&
            content.substring(cursorPosition).equals("$ $$")){
            // delete space - '$ $'
            content = content.delete(cursorPosition, content.length() - 1);
        }

        return content.toString();
    }

    private boolean hasEmptyCharNextToCursor(){
        if((content.charAt(cursorPosition) + "").equals(Constants.EMPTY_CHAR) ||
                (content.charAt(cursorPosition - 1) + "").equals(Constants.EMPTY_CHAR)){
            return true;
        } else {
            return false;
        }
    }
}
