package com.ahmedmatem.lib.mathkeyboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedmatem.lib.mathkeyboard.R;
import com.ahmedmatem.lib.mathkeyboard.contracts.KeyboardClickListener;
import com.ahmedmatem.lib.mathkeyboard.contracts.KeyboardType;
import com.ahmedmatem.lib.mathkeyboard.config.KeyboardConfig;
import com.ahmedmatem.lib.mathkeyboard.contracts.KeyType;
import com.ahmedmatem.lib.mathkeyboard.util.keys.Key;
import com.ahmedmatem.lib.mathkeyboard.util.keys.NavKey;
import com.ahmedmatem.lib.mathkeyboard.util.keys.PrintableKey;
import com.ahmedmatem.lib.mathkeyboard.util.keys.SubmitKey;
import com.bumptech.glide.Glide;

import java.util.List;

public class KeyboardAdapter extends RecyclerView.Adapter<KeyboardAdapter.KeyViewHolder> {
    private Context context;
    private KeyboardClickListener clickListener;
    private int keyboardType;

    private static List<? extends Key> keys;

    public KeyboardAdapter(Context context, KeyboardClickListener listener){

        this(context, listener, KeyboardConfig.DEFAULT_KEYBOARD_TYPE);
    }

    public KeyboardAdapter(Context context, KeyboardClickListener listener, int keyboardType){
        this.context = context;
        clickListener = listener;
        this.keyboardType = keyboardType;
        setKeyboard(keyboardType);
    }

    public int getKeyboardType() {
        return keyboardType;
    }

    @NonNull
    @Override
    public KeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = null;
        switch (viewType) {
            case KeyType.PRINTABLE:
                view = layoutInflater.inflate(R.layout.printable_key, parent, false);
                break;
            case KeyType.DIGIT:
                view = layoutInflater.inflate(R.layout.digit_key, parent, false);
                break;
            case KeyType.NAVIGATION:
                view = layoutInflater.inflate(R.layout.nav_key, parent, false);
                break;
            case KeyType.GROUP:
                view = layoutInflater.inflate(R.layout.group_key, parent, false);
                break;
            case KeyType.SUBMIT:
                view = layoutInflater.inflate(R.layout.submit_key, parent, false);
                break;
            case KeyType.FUNCTIONAL:
                view = layoutInflater.inflate(R.layout.func_key, parent, false);
                break;
        }
//        return null;
        return new KeyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyViewHolder holder, int position) {
        Key key = keys.get(position);

        if (key.hasChildren) {
            holder.dropDownIcon.setVisibility(View.VISIBLE);
        } else {
            holder.dropDownIcon.setVisibility(View.GONE);
        }

        if (key.isImageKey) {
            Glide.with(context)
                    .load(key.getDrawable())
                    .into(holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.textView.setVisibility(View.GONE);
        } else {
            holder.textView.setText(Character.toString((char) key.getUnicode()));
            holder.textView.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
        }

        holder.bind(key, clickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return keys.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public static class KeyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView textView;
        ImageView imageView;
        ImageView dropDownIcon;

        public KeyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            textView = itemView.findViewById(R.id.key_text);
            imageView = itemView.findViewById(R.id.key_image);
            dropDownIcon = itemView.findViewById(R.id.drop_down_icon);
        }

        public <T extends Key> void bind(final T key, final KeyboardClickListener listener) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (key.hasChildren && listener != null) {
                        listener.onKeyLongPress(v, key);
                        return true;
                    }
                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (key instanceof NavKey && listener != null) {
                        listener.onNavKeyClick((NavKey)key);
                    } else if(key instanceof PrintableKey && listener != null){
                        listener.onPrintableKeyClick((PrintableKey) key);
                    } else {
                        // submit key (instance of Key)
                        if(listener != null){
                            listener.onSubmit(key);
                        }
                    }
                }
            });
        }
    }

    public  void updateKeyboard(int keyboardType) {
        setKeyboard(keyboardType);
        notifyDataSetChanged();
    }

    private void setKeyboard(int keyboardType) {
        this.keyboardType = keyboardType;
        switch (keyboardType) {
            case KeyboardType.MATH:
                keys = KeyboardConfig.mathKeys;
                break;
            case KeyboardType.MATH_CAPS:
                keys = KeyboardConfig.mathCapsKeys;
                break;
//            case KeyboardType.EN_SMALL:
//                keys = KeyboardConfig.enSmallKeys;
//                break;
//            case KeyboardType.EN_CAPS:
//                keys = KeyboardConfig.enCapsKeys;
//                break;
            case KeyboardType.BG_SMALL:
                keys = KeyboardConfig.bgSmallKeys;
                break;
            case KeyboardType.BG_CAPS:
                keys = KeyboardConfig.bgCapsKeys;
                break;
        }
    }
}
