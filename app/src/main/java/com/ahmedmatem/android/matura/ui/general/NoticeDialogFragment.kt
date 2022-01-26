package com.ahmedmatem.android.matura.ui.general

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.parcel.Parcelize

private const val ARG_DATA = "data"

@Parcelize
data class NoticeData(
    val title: String?,
    val message: String,
    val positiveButton: String?,
    val negativeButton: String?,
    val neutralButton: String?,
    val tag: String
) : Parcelable

enum class NoticeDialogTag(val value: String) {
    START("start"),
    CHECK("check"),
    STOP("stop"),
    CANCEL("cancel"),
    FINISH("finish");

    companion object {
        private val map = NoticeDialogTag.values()
        fun fromValue(tagValue: String): NoticeDialogTag? {
            values().forEach { tag ->
                if (tag.value == tagValue) {
                    return tag
                }
            }
            return null
        }
    }
}

class NoticeDialogFragment : DialogFragment() {

    internal lateinit var listener: NoticeDialogListener

    private var data: NoticeData? = null
    private var _cancelable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelable(ARG_DATA)
        }
    }

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onDialogNeutralClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity).apply {
                data?.title?.let { this.setTitle(it) }
                data?.message?.let { this.setMessage(it) }
                data?.positiveButton?.let {
                    this.setPositiveButton(it, DialogInterface.OnClickListener { dialog, which ->
                        listener.onDialogPositiveClick(this@NoticeDialogFragment)
                    })
                }
                data?.negativeButton?.let {
                    this.setNegativeButton(it, DialogInterface.OnClickListener { dialog, which ->
                        listener.onDialogNegativeClick(this@NoticeDialogFragment)
                    })
                }
                data?.neutralButton?.let {
                    this.setNeutralButton(it, DialogInterface.OnClickListener { dialog, which ->
                        listener.onDialogNeutralClick(this@NoticeDialogFragment)
                    })
                }
            }
            builder.create().apply {
                isCancelable = _cancelable
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        @JvmStatic
        fun newInstance(
            data: NoticeData,
            listener: NoticeDialogListener,
            cancelable: Boolean = false
        ): NoticeDialogFragment {
            return NoticeDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DATA, data)
                }
                this.listener = listener
                this._cancelable = cancelable
            }
        }
    }
}