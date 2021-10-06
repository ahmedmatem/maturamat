package com.ahmedmatem.android.matura.infrastructure

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {

    /**
     * Use this binding adapter to show and hide the views using boolean variables
     */
    @BindingAdapter("android:fadeVisible")
    @JvmStatic
    fun setVisible(view: View, visible: Boolean? = false) {
        if (view.tag == null) {
            view.tag = true
            view.visibility = if (visible == true) View.VISIBLE else View.GONE
        } else {
            view.animate().cancel()
            if (visible == true) {
                if (view.visibility == View.GONE) {
                    view.fadeIn()
                }
            } else {
                if (view.visibility == View.VISIBLE) {
                    view.fadeOut()
                }
            }
        }
    }
}