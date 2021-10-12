package com.ahmedmatem.android.matura.ui.test.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.ahmedmatem.android.matura.R
import com.bumptech.glide.Glide

@BindingAdapter("emoIcon")
fun fetchImage(view: ImageView, emoInPercent: Int) {
    val resId: Int = when (emoInPercent) {
        in 0..30 -> R.drawable.ic_face_2_24
        in 30..50 -> R.drawable.ic_face_3_24
        in 50..70 -> R.drawable.ic_face_4_24
        in 70..85 -> R.drawable.ic_face_5_24
        else -> R.drawable.ic_face_6_24
    }
    Glide.with(view.context)
        .load(resId)
        .into(view)
}