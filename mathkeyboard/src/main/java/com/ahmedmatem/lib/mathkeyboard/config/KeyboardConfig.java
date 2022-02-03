package com.ahmedmatem.lib.mathkeyboard.config;

import com.ahmedmatem.lib.mathkeyboard.R;
import com.ahmedmatem.lib.mathkeyboard.contracts.KeyboardType;
import com.ahmedmatem.lib.mathkeyboard.contracts.NonPrintableCode;
import com.ahmedmatem.lib.mathkeyboard.util.keys.Key;
import com.ahmedmatem.lib.mathkeyboard.util.keys.DigitKey;
import com.ahmedmatem.lib.mathkeyboard.util.keys.FuncKey;
import com.ahmedmatem.lib.mathkeyboard.util.keys.NavKey;
import com.ahmedmatem.lib.mathkeyboard.util.keys.GroupKey;
import com.ahmedmatem.lib.mathkeyboard.util.keys.PrintableKey;
import com.ahmedmatem.lib.mathkeyboard.util.keys.SubmitKey;

import java.util.ArrayList;
import java.util.List;

public class KeyboardConfig {
    public static final int DEFAULT_SPAN_COUNT = Constants.KEYBOARD_DEFAULT_SPAN_COUNT;
    public static final int MATH_SPAN_COUNT = DEFAULT_SPAN_COUNT;

    public static final int DEFAULT_KEYBOARD_TYPE = KeyboardType.MATH;

    public static int getSpanCount(int type) {
        switch (type){
            case KeyboardType.MATH:
                return MATH_SPAN_COUNT;
            default:
                return DEFAULT_SPAN_COUNT;
        }
    }

    public static final List<? extends Key> mathKeys = new ArrayList<Key>(){
        {
            /**
             * First row
             */

            // AC
            add(new FuncKey(NonPrintableCode.AC, R.drawable.ac_key, null));

            // 1 ... 9 0
            add(new DigitKey('1', null));
            add(new DigitKey('2', null));
            add(new DigitKey('3', null));
            add(new DigitKey('4', null));
            add(new DigitKey('5', null));
            add(new DigitKey('6', null));
            add(new DigitKey('7', null));
            add(new DigitKey('8', null));
            add(new DigitKey('9', null));
            add(new DigitKey('0', null));

            /**
             * Second row
             */

            // a->A
            add(new NavKey(NonPrintableCode.ToUpperCase, R.drawable.to_upper_case_key, null));

            // a b c ... x y z
            add(new PrintableKey('a', null));
            add(new PrintableKey('b', null));
            add(new PrintableKey('c', null));
            add(new GroupKey('d', new ArrayList<Key>(){
                {
                    add(new PrintableKey('d', null));
                    add(new PrintableKey('e', null));
                    add(new PrintableKey('f', null));
                    add(new PrintableKey('g', null));
                }
            }));
            add(new GroupKey('h', new ArrayList<Key>(){
                {
                    add(new PrintableKey('h', null));
                    add(new PrintableKey('k', null));
                    add(new PrintableKey('l', null));
                }
            }));
            add(new GroupKey('m', new ArrayList<Key>(){
                {
                    add(new PrintableKey('m', null));
                    add(new PrintableKey('n', null));
                    add(new PrintableKey('p', null));
                    add(new PrintableKey('g', null));
                }
            }));
            add(new GroupKey('q', new ArrayList<Key>(){
                {
                    add(new PrintableKey('q', null));
                    add(new PrintableKey('r', null));
                    add(new PrintableKey('s', null));
                    add(new PrintableKey('t', null));
                }
            }));
            add(new GroupKey('x', new ArrayList<Key>(){
                {
                    add(new PrintableKey('x', null));
                    add(new PrintableKey('u', null));
                    add(new PrintableKey('v', null));
                }
            }));
            add(new PrintableKey('y', null));
            add(new PrintableKey('z', null));

            /**
             * Third row
             */

            add(new GroupKey('<', new ArrayList<Key>(){
                {
                    add(new PrintableKey('<', null));
                    add(new PrintableKey('≤', null));
                }
            }));
            add(new GroupKey('>', new ArrayList<Key>(){
                {
                    add(new PrintableKey('>', null));
                    add(new PrintableKey('≥', null));
                }
            }));
            add(new PrintableKey('%', null));
            add(new PrintableKey('+', null));
            add(new PrintableKey('-', null));
            add(new PrintableKey(':', new ArrayList<Key>(){
                {
                    add(new PrintableKey(':', null));
                    add(new PrintableKey(';', null));
                }
            }));
            add(new PrintableKey(',', new ArrayList<Key>(){
                {
                    add(new PrintableKey(',', null));
                    add(new PrintableKey('.', null));
                }
            }));
            add(new PrintableKey('=', null)); // span 2

            add(new GroupKey('α', new ArrayList<Key>(){
                {
                    add(new PrintableKey('α', null));
                    add(new PrintableKey('β', null));
                    add(new PrintableKey('γ', null));
                    add(new PrintableKey('ε', null));
                    add(new PrintableKey('λ', null));
                    add(new PrintableKey('μ', null));
                }
            }));// π <π, φ, ω>
            add(new GroupKey('π', new ArrayList<Key>(){
                {
                    add(new PrintableKey('π', null));
                    add(new PrintableKey('φ', null));
                    add(new PrintableKey('ω', null));
                }
            }));

            /**
             * Forth row
             */

            add(new GroupKey('∥', new ArrayList<Key>(){
                {
                    add(new PrintableKey('∥', null));
                    add(new PrintableKey('∦', null));
                }
            }));
            add(new PrintableKey('⊥', null));
            add(new PrintableKey('∆', null));
            // ⋃ <⋃, ⋂>
            add(new GroupKey('⋃', new ArrayList<Key>(){
                {
                    add(new PrintableKey('⋃', null));
                    add(new PrintableKey('⋂', null));
                }
            }));
            add(new PrintableKey('∢', null));
            add(new PrintableKey('°', null));
            add(new PrintableKey('∞', null)); // \∞
            add(new PrintableKey('≌', null));
            add(new PrintableKey('∊', null));
            // ℕ <ℕ, ℚ, ℝ, ℤ>
            add(new GroupKey('ℕ', new ArrayList<Key>(){
                {
                    add(new PrintableKey('ℕ', null)); // \ℕ
                    add(new PrintableKey('ℚ', null)); // \ℚ
                    add(new PrintableKey('ℝ', null)); // \ℝ
                    add(new PrintableKey('ℤ', null)); // \ℤ
                }
            }));
            add(new PrintableKey('∅', null)); // \∅

            /**
             * Fifth (last) row
             */

            // go to text keyboard
            add(new NavKey(NonPrintableCode.ToText, R.drawable.to_text_key, null));
            // olo <olo, o_olo>
            add(new GroupKey(NonPrintableCode.olo, R.drawable.olo_key, new ArrayList<Key>(){
                {
                    add(new PrintableKey(NonPrintableCode.olo, R.drawable.olo_key, null));
                    add(new PrintableKey(NonPrintableCode.o_olo, R.drawable.oolo_key, null));
                }
            }));
            // o_1 <o_1, o_n>
            add(new GroupKey(NonPrintableCode.Index1, R.drawable.index_one_key, new ArrayList<Key>(){
                {
                    add(new PrintableKey(NonPrintableCode.Index1, R.drawable.index_one_key, null));
                    add(new PrintableKey(NonPrintableCode.IndexN, R.drawable.index_o_key, null));
                }
            }));
            // o2 <o2, o3, on>
            add(new GroupKey(NonPrintableCode.Power2, R.drawable.o2_key, new ArrayList<Key>(){
                {
                    add(new PrintableKey(NonPrintableCode.Power2, R.drawable.o2_key, null));
                    add(new PrintableKey(NonPrintableCode.Power3, R.drawable.o3_key, null));
                    add(new PrintableKey(NonPrintableCode.PowerN, R.drawable.oo_key, null));
                }
            }));
            add(new GroupKey(NonPrintableCode.OpenBrackets, R.drawable.brackets_key, new ArrayList<Key>(){
                {
                    add(new PrintableKey(NonPrintableCode.OpenBrackets, R.drawable.brackets_key, null));
                    add(new PrintableKey(NonPrintableCode.OpenLeftBrackets, R.drawable.brackets_open_left_key, null));
                    add(new PrintableKey(NonPrintableCode.OpenRightBrackets, R.drawable.brackets_open_right_key, null));
                    add(new PrintableKey(NonPrintableCode.CloseBrackets, R.drawable.brackets_closed_key, null));
                    add(new PrintableKey(NonPrintableCode.AbsBrackets, R.drawable.abs_key, null));
                }
            }));
            // ( <(, [, |>
            add(new GroupKey('(', new ArrayList<Key>(){
                {
                    add(new PrintableKey('(', null));
                    add(new PrintableKey('[', null));
                    add(new PrintableKey('|', null));
                }
            }));
            // ) <), ], |>
            add(new GroupKey(')', new ArrayList<Key>(){
                {
                    add(new PrintableKey(')', null));
                    add(new PrintableKey(']', null));
                    add(new PrintableKey('|', null));
                }
            }));
            // -->
            add(new FuncKey(NonPrintableCode.LeftArrow, R.drawable.left_arrow_key, null));
            // <--
            add(new FuncKey(NonPrintableCode.RightArrow, R.drawable.right_arrow_key, null));
            // <x|
            add(new FuncKey(NonPrintableCode.Delete, R.drawable.del_key, null));
            // submit key
            add(new SubmitKey(NonPrintableCode.Submit, R.drawable.submit_key, null));
        }
    };

    public static final List<? extends Key> mathCapsKeys = new ArrayList<Key>(){
        {
            /**
             * First row
             */

            // AC
            add(new FuncKey(NonPrintableCode.AC, R.drawable.ac_key, null));

            // 1 ... 9 0
            add(new DigitKey('1', null));
            add(new DigitKey('2', null));
            add(new DigitKey('3', null));
            add(new DigitKey('4', null));
            add(new DigitKey('5', null));
            add(new DigitKey('6', null));
            add(new DigitKey('7', null));
            add(new DigitKey('8', null));
            add(new DigitKey('9', null));
            add(new DigitKey('0', null));

            /**
             * Second row
             */

            // A->a
            add(new NavKey(NonPrintableCode.ToLowerCase, R.drawable.to_lower_case_key, null));

            // A B C ...
            add(new PrintableKey('A', null));
            add(new PrintableKey('B', null));
            add(new PrintableKey('C', null));
            add(new PrintableKey('D', null));
            add(new PrintableKey('E', null));
            add(new GroupKey('F', new ArrayList<Key>(){
                {
                    add(new PrintableKey('F', null));
                    add(new PrintableKey('G', null));
                    add(new PrintableKey('H', null));
                }
            }));
            add(new GroupKey('K', new ArrayList<Key>(){
                {
                    add(new PrintableKey('K', null));
                    add(new PrintableKey('L', null));
                }
            }));
            add(new GroupKey('M', new ArrayList<Key>(){
                {
                    add(new PrintableKey('M', null));
                    add(new PrintableKey('N', null));
                    add(new PrintableKey('O', null));
                }
            }));
            add(new GroupKey('P', new ArrayList<Key>(){
                {
                    add(new PrintableKey('P', null));
                    add(new PrintableKey('Q', null));
                    add(new PrintableKey('R', null));
                }
            }));
            add(new GroupKey('S', new ArrayList<Key>(){
                {
                    add(new PrintableKey('S', null));
                    add(new PrintableKey('T', null));
                    add(new PrintableKey('U', null));
                    add(new PrintableKey('V', null));
                }
            }));

            /**
             * Third row
             */

            add(new GroupKey('<', new ArrayList<Key>(){
                {
                    add(new PrintableKey('<', null));
                    add(new PrintableKey('≤', null));
                }
            }));
            add(new GroupKey('>', new ArrayList<Key>(){
                {
                    add(new PrintableKey('>', null));
                    add(new PrintableKey('≥', null));
                }
            }));
            add(new PrintableKey('%', null));
            add(new PrintableKey('+', null));
            add(new PrintableKey('-', null));
            add(new PrintableKey(':', new ArrayList<Key>(){
                {
                    add(new PrintableKey(':', null));
                    add(new PrintableKey(';', null));
                }
            }));
            add(new PrintableKey(',', new ArrayList<Key>(){
                {
                    add(new PrintableKey(',', null));
                    add(new PrintableKey('.', null));
                }
            }));
            add(new PrintableKey('=', null)); // span 2

            add(new GroupKey('α', new ArrayList<Key>(){
                {
                    add(new PrintableKey('α', null));
                    add(new PrintableKey('β', null));
                    add(new PrintableKey('γ', null));
                    add(new PrintableKey('ε', null));
                    add(new PrintableKey('λ', null));
                    add(new PrintableKey('μ', null));
                }
            }));
            // π <π, φ, ω>
            add(new GroupKey('π', new ArrayList<Key>(){
                {
                    add(new PrintableKey('π', null));
                    add(new PrintableKey('φ', null));
                    add(new PrintableKey('ω', null));
                }
            }));

            /**
             * Forth row
             */

            add(new GroupKey('∥', new ArrayList<Key>(){
                {
                    add(new PrintableKey('∥', null));
                    add(new PrintableKey('∦', null));
                }
            }));
            add(new PrintableKey('⊥', null));
            add(new PrintableKey('∆', null));
            // ⋃ <⋃, ⋂>
            add(new GroupKey('⋃', new ArrayList<Key>(){
                {
                    add(new PrintableKey('⋃', null));
                    add(new PrintableKey('⋂', null));
                }
            }));
            add(new PrintableKey('∢', null));
            add(new PrintableKey('°', null));
            add(new PrintableKey('∞', null)); // \∞
            add(new PrintableKey('≌', null));
            add(new PrintableKey('∊', null));
            // ℕ <ℕ, ℚ, ℝ, ℤ>
            add(new GroupKey('ℕ', new ArrayList<Key>(){
                {
                    add(new PrintableKey('ℕ', null)); // \ℕ
                    add(new PrintableKey('ℚ', null)); // \ℚ
                    add(new PrintableKey('ℝ', null)); // \ℝ
                    add(new PrintableKey('ℤ', null)); // \ℤ
                }
            }));
            add(new PrintableKey('∅', null)); // \∅

            /**
             * Fifth (last) row
             */

            // go to text keyboard
            add(new NavKey(NonPrintableCode.ToText, R.drawable.to_text_key, null));
            // olo <olo, o_olo>
            add(new GroupKey(NonPrintableCode.olo, R.drawable.olo_key, new ArrayList<Key>(){
                {
                    add(new PrintableKey(NonPrintableCode.olo, R.drawable.olo_key, null));
                    add(new PrintableKey(NonPrintableCode.o_olo, R.drawable.oolo_key, null));
                }
            }));
            // o_1 <o_1, o_n>
            add(new GroupKey(NonPrintableCode.Index1, R.drawable.index_one_key, new ArrayList<Key>(){
                {
                    add(new PrintableKey(NonPrintableCode.Index1, R.drawable.index_one_key, null));
                    add(new PrintableKey(NonPrintableCode.IndexN, R.drawable.index_o_key, null));
                }
            }));
            // o2 <o2, o3, on>
            add(new GroupKey(NonPrintableCode.Power2, R.drawable.o2_key, new ArrayList<Key>(){
                {
                    add(new PrintableKey(NonPrintableCode.Power2, R.drawable.o2_key, null));
                    add(new PrintableKey(NonPrintableCode.Power3, R.drawable.o3_key, null));
                    add(new PrintableKey(NonPrintableCode.PowerN, R.drawable.oo_key, null));
                }
            }));
            add(new GroupKey(NonPrintableCode.OpenBrackets, R.drawable.brackets_key, new ArrayList<Key>(){
                {
                    add(new PrintableKey(NonPrintableCode.OpenBrackets, R.drawable.brackets_key, null));
                    add(new PrintableKey(NonPrintableCode.OpenLeftBrackets, R.drawable.brackets_open_left_key, null));
                    add(new PrintableKey(NonPrintableCode.OpenRightBrackets, R.drawable.brackets_open_right_key, null));
                    add(new PrintableKey(NonPrintableCode.CloseBrackets, R.drawable.brackets_closed_key, null));
                    add(new PrintableKey(NonPrintableCode.AbsBrackets, R.drawable.abs_key, null));
                }
            }));
            // ( <(, [, |>
            add(new GroupKey('(', new ArrayList<Key>(){
                {
                    add(new PrintableKey('(', null));
                    add(new PrintableKey('[', null));
                    add(new PrintableKey('|', null));
                }
            }));
            // ) <), ], |>
            add(new GroupKey(')', new ArrayList<Key>(){
                {
                    add(new PrintableKey(')', null));
                    add(new PrintableKey(']', null));
                    add(new PrintableKey('|', null));
                }
            }));
            // -->
            add(new FuncKey(NonPrintableCode.LeftArrow, R.drawable.left_arrow_key, null));
            // <--
            add(new FuncKey(NonPrintableCode.RightArrow, R.drawable.right_arrow_key, null));
            // <x|
            add(new FuncKey(NonPrintableCode.Delete, R.drawable.del_key, null));
            // submit key
            add(new SubmitKey(NonPrintableCode.Submit, R.drawable.submit_key, null));
        }
    };

    // region ENGLISH KEYS

//    public static final List<? extends Key> enSmallKeys = new ArrayList<Key>(){
//        {
//            add(new PrintableKey('a', null));
//            add(new PrintableKey('b', null));
//            add(new PrintableKey('c', null));
//            add(new PrintableKey('d', null));
//            add(new PrintableKey('e', null));
//            add(new PrintableKey('f', null));
//            add(new PrintableKey('g', null));
//            add(new PrintableKey('h', null));
//
//            add(new PrintableKey('i', null));
//            add(new PrintableKey('j', null));
//            add(new PrintableKey('k', null));
//            add(new PrintableKey('l', null));
//            add(new PrintableKey('m', null));
//            add(new PrintableKey('n', null));
//            add(new PrintableKey('o', null));
//            add(new PrintableKey('p', null));
//
//            add(new NavKey(NonPrintableCode.ToUpperCase,R.drawable.to_upper_case_key, null));
//            add(new PrintableKey('q', null));
//            add(new PrintableKey('r', null));
//            add(new PrintableKey('s', null));
//            add(new PrintableKey('t', null));
//            add(new PrintableKey('u', null));
//            add(new PrintableKey('v', null));
//            add(new PrintableKey('w', null));
//
//            add(new NavKey(NonPrintableCode.BgLang, R.drawable.bg_lang_key, null));
//            add(new PrintableKey('x', null));
//            add(new PrintableKey('y', null));
//            add(new PrintableKey('z', null));
//            add(new PrintableKey(0x03B1, R.drawable.alpha_key, null));
//            add(new PrintableKey(0x03B2, R.drawable.beta_key, null));
//            add(new PrintableKey(0x03B3, R.drawable.gamma_key, null));
//            add(new PrintableKey(0x03C0, R.drawable.pi_key, null));
//
//            // to math expression navigation key
//            add(new NavKey(NonPrintableCode.ToMath, R.drawable.to_math_key, null));
//
//            // <-
//            add(new FuncKey(NonPrintableCode.LeftArrow, R.drawable.left_arrow_key, null));
//
//            // ->
//            add(new FuncKey(NonPrintableCode.RightArrow, R.drawable.right_arrow_key, null));
//
//            // <x|
//            add(new FuncKey(NonPrintableCode.Delete, R.drawable.del_key, null));
//
//            add(new PrintableKey('(', null));
//            add(new PrintableKey(',', null));
//            add(new PrintableKey(')', null));
//
//            // submit
//            add(new SubmitKey(NonPrintableCode.Submit, R.drawable.submit_key, null));
//        }
//    };
//
//    public static final List<? extends Key> enCapsKeys = new ArrayList<Key>(){
//        {
//            add(new PrintableKey('A', null));
//            add(new PrintableKey('B', null));
//            add(new PrintableKey('C', null));
//            add(new PrintableKey('D', null));
//            add(new PrintableKey('E', null));
//            add(new PrintableKey('F', null));
//            add(new PrintableKey('G', null));
//            add(new PrintableKey('H', null));
//
//            add(new PrintableKey('I', null));
//            add(new PrintableKey('J', null));
//            add(new PrintableKey('K', null));
//            add(new PrintableKey('L', null));
//            add(new PrintableKey('M', null));
//            add(new PrintableKey('N', null));
//            add(new PrintableKey('O', null));
//            add(new PrintableKey('P', null));
//
//            add(new NavKey(NonPrintableCode.ToLowerCase,R.drawable.to_lower_case_key, null));
//            add(new PrintableKey('Q', null));
//            add(new PrintableKey('R', null));
//            add(new PrintableKey('S', null));
//            add(new PrintableKey('T', null));
//            add(new PrintableKey('U', null));
//            add(new PrintableKey('V', null));
//            add(new PrintableKey('W', null));
//
//            add(new NavKey(NonPrintableCode.BgLang, R.drawable.bg_lang_key, null));
//            add(new PrintableKey('X', null));
//            add(new PrintableKey('Y', null));
//            add(new PrintableKey('Z', null));
//            add(new PrintableKey(0x03B1, R.drawable.alpha_key, null));
//            add(new PrintableKey(0x03B2, R.drawable.beta_key, null));
//            add(new PrintableKey(0x03B3, R.drawable.gamma_key, null));
//            add(new PrintableKey(0x03C0, R.drawable.pi_key, null));
//
//            // to math expression navigation key
//            add(new NavKey(NonPrintableCode.ToMath, R.drawable.to_math_key, null));
//
//            // <-
//            add(new FuncKey(NonPrintableCode.LeftArrow, R.drawable.left_arrow_key, null));
//
//            // ->
//            add(new FuncKey(NonPrintableCode.RightArrow, R.drawable.right_arrow_key, null));
//
//            // <x|
//            add(new FuncKey(NonPrintableCode.Delete, R.drawable.del_key, null));
//
//            add(new PrintableKey('(', null));
//            add(new PrintableKey(',', null));
//            add(new PrintableKey(')', null));
//
//            // submit
//            add(new SubmitKey(NonPrintableCode.Submit, R.drawable.submit_key, null));
//        }
//    };

    // endregion

    public static final List<? extends Key> bgSmallKeys = new ArrayList<Key>(){
        {
            /**
             * First row
             */

            // AC
            add(new FuncKey(NonPrintableCode.AC, R.drawable.ac_key, null));

            // 1 ... 9 0
            add(new DigitKey('1', null));
            add(new DigitKey('2', null));
            add(new DigitKey('3', null));
            add(new DigitKey('4', null));
            add(new DigitKey('5', null));
            add(new DigitKey('6', null));
            add(new DigitKey('7', null));
            add(new DigitKey('8', null));
            add(new DigitKey('9', null));
            add(new DigitKey('0', null));

            /**
             * Second row
             */

            // a->A
            add(new NavKey(NonPrintableCode.ToUpperCase, R.drawable.to_upper_case_key, null));
            add(new PrintableKey('я', null));
            add(new PrintableKey('в', null));
            add(new PrintableKey('е', null));
            add(new PrintableKey('р', null));
            add(new PrintableKey('т', null));
            add(new PrintableKey('ъ', null));
            add(new PrintableKey('у', null));
            add(new PrintableKey('и', null));
            add(new PrintableKey('о', null));
            add(new PrintableKey('п', null));

            /**
             * Third row
             */

            add(new PrintableKey('а', null));
            add(new PrintableKey('с', null));
            add(new PrintableKey('д', null));
            add(new PrintableKey('ф', null));
            add(new PrintableKey('г', null));
            add(new PrintableKey('х', null));
            add(new PrintableKey('й', null));
            add(new PrintableKey('к', null));
            add(new PrintableKey('л', null));
            add(new PrintableKey('ш', null));
            add(new PrintableKey('.', null));

            /**
             * Forth row
             */

            add(new PrintableKey('з', null));
            add(new PrintableKey('ь', null));
            add(new PrintableKey('ц', null));
            add(new PrintableKey('ж', null));
            add(new PrintableKey('б', null));
            add(new PrintableKey('н', null));
            add(new PrintableKey('м', null));
            add(new PrintableKey('ю', null));
            add(new PrintableKey('ч', null));
            add(new PrintableKey('щ', null));
            add(new PrintableKey(',', null));

            /**
             * Fifth (last) row
             */

            // to math keyboard
            add(new NavKey(NonPrintableCode.ToMath, R.drawable.to_math_key, null));
            add(new PrintableKey(';', null));
            add(new PrintableKey('-', null));
            add(new PrintableKey(':', null));
            // interval " "
            add(new FuncKey((int) ' ', R.drawable.interval_key, null));
            // <-
            add(new FuncKey(NonPrintableCode.LeftArrow, R.drawable.left_arrow_key, null));
            // ->
            add(new FuncKey(NonPrintableCode.RightArrow, R.drawable.right_arrow_key, null));
            // <x|
            add(new FuncKey(NonPrintableCode.Delete, R.drawable.del_key, null));
            // submit
            add(new SubmitKey(NonPrintableCode.Submit, R.drawable.submit_key, null));
        }
    };

    public static final List<? extends Key> bgCapsKeys = new ArrayList<Key>(){
        {
            /**
             * First row
             */

            // AC
            add(new FuncKey(NonPrintableCode.AC, R.drawable.ac_key, null));

            // 1 ... 9 0
            add(new DigitKey('1', null));
            add(new DigitKey('2', null));
            add(new DigitKey('3', null));
            add(new DigitKey('4', null));
            add(new DigitKey('5', null));
            add(new DigitKey('6', null));
            add(new DigitKey('7', null));
            add(new DigitKey('8', null));
            add(new DigitKey('9', null));
            add(new DigitKey('0', null));

            /**
             * Second row
             */

            // A->a
            add(new NavKey(NonPrintableCode.ToLowerCase, R.drawable.to_lower_case_key, null));
            add(new PrintableKey('Я', null));
            add(new PrintableKey('В', null));
            add(new PrintableKey('Е', null));
            add(new PrintableKey('Р', null));
            add(new PrintableKey('Т', null));
            add(new PrintableKey('Ъ', null));
            add(new PrintableKey('У', null));
            add(new PrintableKey('И', null));
            add(new PrintableKey('О', null));
            add(new PrintableKey('П', null));

            /**
             * Third row
             */

            add(new PrintableKey('А', null));
            add(new PrintableKey('С', null));
            add(new PrintableKey('Д', null));
            add(new PrintableKey('Ф', null));
            add(new PrintableKey('Г', null));
            add(new PrintableKey('Х', null));
            add(new PrintableKey('Й', null));
            add(new PrintableKey('К', null));
            add(new PrintableKey('Л', null));
            add(new PrintableKey('Ш', null));
            add(new PrintableKey('.', null));

            /**
             * Forth row
             */

            add(new PrintableKey('З', null));
            add(new PrintableKey('ь', null));
            add(new PrintableKey('Ц', null));
            add(new PrintableKey('Ж', null));
            add(new PrintableKey('Б', null));
            add(new PrintableKey('Н', null));
            add(new PrintableKey('М', null));
            add(new PrintableKey('Ю', null));
            add(new PrintableKey('Ч', null));
            add(new PrintableKey('Щ', null));
            add(new PrintableKey(',', null));

            /**
             * Fifth (last) row
             */

            // to math keyboard
            add(new NavKey(NonPrintableCode.ToMath, R.drawable.to_math_key, null));
            add(new PrintableKey(';', null));
            add(new PrintableKey('-', null));
            add(new PrintableKey(':', null));
            // interval " "
            add(new FuncKey((int) ' ', R.drawable.interval_key, null));
            // <-
            add(new FuncKey(NonPrintableCode.LeftArrow, R.drawable.left_arrow_key, null));
            // ->
            add(new FuncKey(NonPrintableCode.RightArrow, R.drawable.right_arrow_key, null));
            // <x|
            add(new FuncKey(NonPrintableCode.Delete, R.drawable.del_key, null));
            // submit
            add(new SubmitKey(NonPrintableCode.Submit, R.drawable.submit_key, null));
        }
    };
}
