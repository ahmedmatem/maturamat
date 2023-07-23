package com.ahmedmatem.android.matura.ui.test2

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.databinding.Test2SubmitConfirmDialogBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Test2SubmitConfirmDialog : DialogFragment() {

    val viewModel: NewTest2ViewModel by navGraphViewModels(R.id.nav_graph_test_2)

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
                viewModel.onDialogPositiveButtonClick()
                viewModel.onDialogPositiveButtonClickComplete()
                /** Disable button after click */
                viewModel.setDialogPositiveButtonEnabled(false)
            }
        }

        lifecycleScope.launch {
            viewModel.isDialogPositiveButtonEnabled.collect { isEnabled ->
                (dialog as AlertDialog)
                    .getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = isEnabled
            }
        }

        /**
         * Collect progress 1 information and update dialog UI
         */
        lifecycleScope.launch {
            viewModel.progressState1.collect { progressTo ->
                val currentProgress = binding.progressBar1.progress
                val animation: ObjectAnimator = ObjectAnimator.ofInt(
                    binding.progressBar1, "progress", currentProgress, progressTo)
                animation.duration = ANIMATION_DURATION
                animation.interpolator = LinearInterpolator()
                animation.start()
            }
        }

        /**
         * Collect progress 2 information and update dialog UI
         */
        lifecycleScope.launch {
            viewModel.progressState2.collect { progressTo ->
                val currentProgress = binding.progressBar2.progress
                val animation: ObjectAnimator = ObjectAnimator.ofInt(
                    binding.progressBar2, "progress", currentProgress, progressTo)
                animation.duration = ANIMATION_DURATION
                animation.interpolator = LinearInterpolator()
                animation.start()
            }
        }

        /**
         * Collect progress 3 information and update dialog UI
         */
        lifecycleScope.launch {
            viewModel.progressState3.collect { progressTo ->
                val currentProgress = binding.progressBar3.progress
                val animation: ObjectAnimator = ObjectAnimator.ofInt(
                    binding.progressBar3, "progress", currentProgress, progressTo)
                animation.duration = ANIMATION_DURATION
                animation.interpolator = LinearInterpolator()
                animation.start()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        _binding = null
    }

    companion object {
        const val TAG = "Test2SubmitDialog"
        const val ANIMATION_DURATION = 2000L
    }
}