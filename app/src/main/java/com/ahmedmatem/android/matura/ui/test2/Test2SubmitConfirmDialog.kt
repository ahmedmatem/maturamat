package com.ahmedmatem.android.matura.ui.test2

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.databinding.Test2SubmitConfirmDialogBinding
import java.lang.ClassCastException

class Test2SubmitConfirmDialog(private val listener: SubmitDialogInterface) : DialogFragment() {

    interface SubmitDialogInterface {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    private var _binding: Test2SubmitConfirmDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            _binding = Test2SubmitConfirmDialogBinding.inflate(requireActivity().layoutInflater)
            val builder = AlertDialog.Builder(requireContext()).apply {
                setTitle(getString(R.string.solutions))
                setView(binding.root)
                /**
                 * Set click listener to null in order to prevent dialog dismissing
                 * and define custom click listener later onStart()
                 * */
                setPositiveButton(getString(R.string.send_button), null)
                setNeutralButton(getString(R.string.cancel_button)) { dialog, which ->
                    // todo: Not yet implemented.
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        dialog?.also {
            (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                listener?.let {
                    listener.onDialogPositiveClick(this@Test2SubmitConfirmDialog)
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        _binding = null
    }

    companion object {
        const val TAG = "Test2SubmitConfirmDialog"
    }
}