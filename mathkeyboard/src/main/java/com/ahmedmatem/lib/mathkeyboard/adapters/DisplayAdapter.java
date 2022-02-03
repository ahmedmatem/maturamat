package com.ahmedmatem.lib.mathkeyboard.adapters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ahmedmatem.lib.mathkeyboard.config.Constants;
import com.ahmedmatem.lib.mathkeyboard.ui.DisplayView;
import com.ahmedmatem.lib.mathkeyboard.util.DisplayContent;
import com.ahmedmatem.lib.mathkeyboard.contracts.NonPrintableCode;
import com.ahmedmatem.lib.mathkeyboard.util.keys.PrintableKey;

public class DisplayAdapter {
    private DisplayView displayView;

    private final DisplayContent displayContent = new DisplayContent();

    public DisplayAdapter(DisplayView displayView) {
        this.displayView = displayView;
    }

    public void update(PrintableKey key) {
        int cursorPos = displayContent.cursorPosition;
        StringBuilder content = displayContent.content;
        int keyCode = key.getUnicode();
        switch (keyCode) {
            /**
             * none printable keys
             */
            case NonPrintableCode.Index1: // x_1
                if(cursorPos > 1){
                    if(isAlphanumeric(content.charAt(cursorPos - 1))){
                        displayContent.insert("_{1}", 4);
                    } else {
                        displayContent.insert("{{" + Constants.EMPTY_CHAR + "}_{1}}", 2);
                    }
                }
                break;
            case NonPrintableCode.IndexN: // x_n
                if(cursorPos > 1){
                    if(isAlphanumeric(content.charAt(cursorPos - 1))){
                        displayContent.insert("_{" + Constants.EMPTY_CHAR + "}", 2);
                    } else {
                        displayContent.insert("{{" + Constants.EMPTY_CHAR + "}_{" + Constants.EMPTY_CHAR + "}}", 2);
                    }
                } else {
                    displayContent.insert("{{" + Constants.EMPTY_CHAR + "}_{" + Constants.EMPTY_CHAR + "}}", 2);
                }
                break;
            case NonPrintableCode.Power2: // x^2
                if(cursorPos > 1){
                    if(isAlphanumeric(content.charAt(cursorPos - 1)) ||
                            (content.charAt(cursorPos - 1) + "").equals(")")){
                        displayContent.insert("^{2}", 4);
                    } else {
                        displayContent.insert("{{" + Constants.EMPTY_CHAR + "}^{2}}", 2);
                    }
                } else {
                    displayContent.insert("{{" + Constants.EMPTY_CHAR + "}^{2}}", 2);
                }
                break;
            case NonPrintableCode.Power3: // x^3
                if(cursorPos > 1){
                    if(isAlphanumeric(content.charAt(cursorPos - 1)) ||
                            (content.charAt(cursorPos - 1) + "").equals(")")){
                        displayContent.insert("^{3}", 4);
                    } else {
                        displayContent.insert("{{" + Constants.EMPTY_CHAR + "}^{3}}", 2);
                    }
                } else {
                    displayContent.insert("{{" + Constants.EMPTY_CHAR + "}^{3}}", 2);
                }
                break;
            case NonPrintableCode.PowerN: // x^n
                if(cursorPos > 1){
                    Pattern pattern = Pattern.compile("[\\(|\\]]");
                    if(isAlphanumeric(content.charAt(cursorPos - 1)) ||
                            pattern.matcher(content.charAt(cursorPos - 1) + "").find()){
                        displayContent.insert("^{" + Constants.EMPTY_CHAR + "}", 2);
                    } else {
                        displayContent.insert("{{" + Constants.EMPTY_CHAR + "}^{" + Constants.EMPTY_CHAR + "}}", 2);
                    }
                } else {
                    displayContent.insert("{{" + Constants.EMPTY_CHAR + "}^{" + Constants.EMPTY_CHAR + "}}", 2);
                }
                break;
            case NonPrintableCode.olo: // a/b
                displayContent.insert("{{" + Constants.EMPTY_CHAR + "}/{" + Constants.EMPTY_CHAR + "}}", 2);
                break;
            case NonPrintableCode.o_olo: // a{b/c}
                displayContent.insert("{" + Constants.EMPTY_CHAR + "}{{" + Constants.EMPTY_CHAR + "}/{" + Constants.EMPTY_CHAR + "}}");
                break;
            case NonPrintableCode.OpenBrackets: // (x)
                displayContent.insert("(" + Constants.EMPTY_CHAR + ")");
                break;
            case NonPrintableCode.OpenLeftBrackets: // (x]
                displayContent.insert("(" + Constants.EMPTY_CHAR + "]");
                break;
            case NonPrintableCode.OpenRightBrackets: // [x)
                displayContent.insert("[" + Constants.EMPTY_CHAR + ")");
                break;
            case NonPrintableCode.CloseBrackets: // [x]
                displayContent.insert("[" + Constants.EMPTY_CHAR + "]");
                break;
            case NonPrintableCode.AbsBrackets: // |x|
                displayContent.insert("|" + Constants.EMPTY_CHAR + "|");
                break;

            /**
             * functional keys
             */

            case NonPrintableCode.AC:
                displayContent.clear();
                break;
            case NonPrintableCode.LeftArrow:
                displayContent.moveCursorLeft();
                break;
            case NonPrintableCode.RightArrow:
                displayContent.moveCursorRight();
                break;
            case NonPrintableCode.Delete:
                displayContent.deleteBack();
                break;

            /**
             * printable keys
             */

            default:
                displayContent.insert((char)key.getUnicode() + "");
                break;
        }

        displayView.print(displayContent);
    }

    public DisplayContent getDisplayContent(){
        return displayContent;
    }

    public DisplayView getDisplayView(){
        return displayView;
    }

    // region helpers

    private boolean isAlphanumeric(char ch){
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]");
        Matcher matcher = pattern.matcher(ch + "");
        return matcher.find();
    }

    // endregion
}
