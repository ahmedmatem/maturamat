package com.ahmedmatem.lib.mathkeyboard.util;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ahmedmatem.lib.mathkeyboard.R;
import com.ahmedmatem.lib.mathkeyboard.contracts.KeyboardClickListener;
import com.ahmedmatem.lib.mathkeyboard.util.keys.Key;
import com.ahmedmatem.lib.mathkeyboard.util.keys.PrintableKey;
import com.bumptech.glide.Glide;

import java.util.List;

public class KeyboardPopup {
    private KeyboardClickListener listener;

    private PopupWindow popupWindow;

    public KeyboardPopup(KeyboardClickListener listener) {
        this.listener = listener;
    }

    public void showPopupWindow(final View view,
                                List<? extends Key> keys) {
        Rect locationRect = locateView(view);

        LayoutInflater inflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        createPopupView(inflater, popupView, keys);

        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                view.getResources().getInteger(R.integer.key_height) +
                        2 * (view.getResources().getInteger(R.integer.key_layout_margin) +
                                view.getResources().getInteger(R.integer.popup_layout_padding)),
                view.getContext().getResources().getDisplayMetrics()));
        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, locationRect.left,
                locationRect.top - popupWindow.getHeight());
    }

    private void createPopupView(LayoutInflater inflater, View popupView, List<? extends Key> keys) {
        LinearLayout popupLayout = popupView.findViewById(R.id.popup_layout_id);
        for (final Key key : keys) {
            final View v = inflateKey(inflater, popupLayout, key);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(key instanceof PrintableKey && listener != null){
                        listener.onPrintableKeyClick((PrintableKey) key);
                        popupWindow.dismiss();
                    }
                }
            });
            popupLayout.addView(v);
        }
    }

    private View inflateKey(LayoutInflater inflater, ViewGroup root, Key key) {
        View view = inflater.inflate(R.layout.popup_key, root, false);
        if (key.isImageKey) {
            ImageView imageView = view.findViewById(R.id.key_image);
            Glide.with(view.getContext())
                    .load(key.getDrawable())
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);
        } else {
            TextView textView = view.findViewById(R.id.key_text);
            textView.setText(Character.toString((char) key.getUnicode()));
            textView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null)
            return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = loc_int[0] + v.getWidth();
        location.bottom = loc_int[1] + v.getHeight();
        return location;
    }
}
