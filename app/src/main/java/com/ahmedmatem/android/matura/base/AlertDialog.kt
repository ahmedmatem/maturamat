package com.ahmedmatem.android.matura.base

import android.app.Activity
import androidx.fragment.app.DialogFragment
import kotlin.ClassCastException

class AlertDialog : DialogFragment() {

    internal lateinit var listener: AlertDialogListener

    interface AlertDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, tag: Any?)
        fun onDialogNegativeClick(dialog: DialogFragment, tag: Any?)
        fun onDialogNeutralClick(dialog: DialogFragment, tag: Any?)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            listener = context as AlertDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement AlertDialogListener")
        }
    }
}