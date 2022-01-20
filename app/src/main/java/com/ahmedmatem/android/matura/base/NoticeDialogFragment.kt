package com.ahmedmatem.android.matura.base

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.parcel.Parcelize
import kotlin.ClassCastException

private const val ARG_DATA = "data"

@Parcelize
data class NoticeData(
    val title: String?,
    val message: String,
    val positiveButton: String?,
    val negativeButton: String?,
    val neutralButton: String?,
    val tag: String?
) : Parcelable

class NoticeDialogFragment : DialogFragment() {

    internal lateinit var listener: NoticeDialogListener

    private var data: NoticeData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelable(ARG_DATA)
        }
    }

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, tag: Any?)
        fun onDialogNegativeClick(dialog: DialogFragment, tag: Any?)
        fun onDialogNeutralClick(dialog: DialogFragment, tag: Any?)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity).apply {
                data?.title?.let { this.setTitle(it) }
                data?.message?.let { this.setMessage(it) }
                data?.positiveButton?.let {
                    this.setPositiveButton(it, DialogInterface.OnClickListener { dialog, which ->
                        listener.onDialogPositiveClick(this@NoticeDialogFragment, data?.tag)
                    })
                }
                data?.negativeButton?.let {
                    this.setNegativeButton(it, DialogInterface.OnClickListener { dialog, which ->
                        listener.onDialogNegativeClick(this@NoticeDialogFragment, data?.tag)
                    })
                }
                data?.neutralButton?.let {
                    this.setNeutralButton(it, DialogInterface.OnClickListener { dialog, which ->
                        listener.onDialogNeutralClick(this@NoticeDialogFragment, data?.tag)
                    })
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement AlertDialogListener")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(data: NoticeData) {
            NoticeDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DATA, data)
                }
            }
        }
    }
}