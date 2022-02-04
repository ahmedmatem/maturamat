package com.ahmedmatem.lib.mathkeyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ahmedmatem.lib.mathkeyboard.adapters.DisplayAdapter;
import com.ahmedmatem.lib.mathkeyboard.adapters.KeyboardAdapter;
import com.ahmedmatem.lib.mathkeyboard.config.KeyboardConfig;
import com.ahmedmatem.lib.mathkeyboard.contracts.KeyboardExternalListener;
import com.ahmedmatem.lib.mathkeyboard.contracts.NonPrintableCode;
import com.ahmedmatem.lib.mathkeyboard.contracts.KeyboardClickListener;
import com.ahmedmatem.lib.mathkeyboard.contracts.KeyboardType;
import com.ahmedmatem.lib.mathkeyboard.ui.DisplayView;
import com.ahmedmatem.lib.mathkeyboard.util.DisplayContent;
import com.ahmedmatem.lib.mathkeyboard.util.KeyboardPopup;
import com.ahmedmatem.lib.mathkeyboard.util.keys.Key;
import com.ahmedmatem.lib.mathkeyboard.util.keys.NavKey;
import com.ahmedmatem.lib.mathkeyboard.util.keys.PrintableKey;

public class MathInputEditorFragment extends Fragment implements KeyboardClickListener {

    private KeyboardAdapter keyboardAdapter;
    private DisplayAdapter displayAdapter;
    private ImageView closeButton;

    private GridLayoutManager layoutManager;

    private KeyboardExternalListener listener;

    // UI elements
    private DisplayView displayView; //  extends WebView
    private RecyclerView keyboardView;

    public String divSelector; // set it in showKeyboard(selector) javascript interface at app module

    public MathInputEditorFragment() {
        // Required empty public constructor
    }

    public static MathInputEditorFragment newInstance() {
        MathInputEditorFragment fragment = new MathInputEditorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =
                inflater.inflate(R.layout.fragment_math_input_editor, container, false);

        // display
        displayView = view.findViewById(R.id.keyboard_display);
        displayView.getSettings().setJavaScriptEnabled(true);
        displayView.loadUrl("file:///android_asset/keyboard-display.html");

        // close button
        closeButton = view.findViewById(R.id.btn_keyboard_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onKeyboardCloseBtnClick();
                }
            }
        });

        // keyboard
        keyboardView = view.findViewById(R.id.keyboard_rv);
        keyboardView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(),
                KeyboardConfig.getSpanCount(KeyboardConfig.DEFAULT_KEYBOARD_TYPE));
        keyboardView.setLayoutManager(layoutManager);

        // adapters
        displayAdapter = new DisplayAdapter(displayView);
        keyboardAdapter = new KeyboardAdapter(getContext(), this);

        keyboardView.setAdapter(keyboardAdapter);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int keyboardType = keyboardAdapter.getKeyboardType();
                if(keyboardType == KeyboardType.BG_SMALL
                || keyboardType == KeyboardType.BG_CAPS) {
                    if (position == keyboardAdapter.getItemCount() - 5) { // space key
                        return 3;
                    }
                    return 1;
                } else if(keyboardType == KeyboardType.MATH ||
                            keyboardType == KeyboardType.MATH_CAPS){
                    if(position == (3 * KeyboardConfig.MATH_SPAN_COUNT - 4)){ // third row, third position from right
                        return 2;
                    }
                }
                return 1;
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        if(context instanceof KeyboardExternalListener){
//            listener = (KeyboardExternalListener) context;
//        } else {
//            throw new ClassCastException(context.getClass().getSimpleName() +
//                    " must implement KeyboardExternalListener interface");
//        }
    }

    @Override
    public <T extends Key> void onKeyLongPress(View v, T key) {
        KeyboardPopup keyboardPopup = new KeyboardPopup(this);
        keyboardPopup.showPopupWindow(v, key.children);
    }

    @Override
    public void onPrintableKeyClick(PrintableKey key) {
        displayAdapter.update(key);
    }

    @Override
    public void onNavKeyClick(NavKey key) {
        int keyCode = key.getUnicode();
        int keyboardType = KeyboardType.DEFAULT;
        switch (keyCode) {
            case NonPrintableCode.ToMath:
//                layoutManager.setSpanCount(KeyboardConfig.MATH_SPAN_COUNT);
                keyboardType = KeyboardType.MATH;
                break;
            case NonPrintableCode.ToText:
                keyboardType = KeyboardType.BG_SMALL;
                break;
            case NonPrintableCode.ToLowerCase:
                switch (keyboardAdapter.getKeyboardType()) {
                    case KeyboardType.MATH_CAPS:
                        keyboardType = KeyboardType.MATH;
                        break;
                    case KeyboardType.BG_CAPS:
                        keyboardType = KeyboardType.BG_SMALL;
                        break;
                }
                break;
            case NonPrintableCode.ToUpperCase:
                switch (keyboardAdapter.getKeyboardType()) {
                    case KeyboardType.MATH:
                        keyboardType = KeyboardType.MATH_CAPS;
                        break;
                    case KeyboardType.BG_SMALL:
                        keyboardType = KeyboardType.BG_CAPS;
                        break;
                }
                break;
        }

        keyboardAdapter.updateKeyboard(keyboardType);
    }

    @Override
    public void onSubmit(Key key) {
        if (listener != null) {
            listener.onKeyboardSubmit(divSelector);
        }
    }

    public DisplayView getDisplayView(){
        return displayAdapter.getDisplayView();
    }

    public DisplayContent getDisplayContent(){
        return displayAdapter.getDisplayContent();
    }
}