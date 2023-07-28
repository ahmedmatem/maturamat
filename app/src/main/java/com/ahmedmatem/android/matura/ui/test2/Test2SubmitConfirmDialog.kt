package com.ahmedmatem.android.matura.ui.test2

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.databinding.Test2SubmitConfirmDialogBinding
import com.ahmedmatem.android.matura.utils.animate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Test2SubmitConfirmDialog : DialogFragment() {

    val viewModel: NewTest2ViewModel by navGraphViewModels(R.id.nav_graph_test_2)

    private var _binding: Test2SubmitConfirmDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            _binding = Test2SubmitConfirmDialogBinding.inflate(requireActivity().layoutInflater)

            /** Initialize dialog UI */
            initUI()

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
            }
        }

        lifecycleScope.launch {
            viewModel.dialogPositiveButtonEnabled.collect { isEnabled ->
                (dialog as AlertDialog)
                    .getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = isEnabled
            }
        }

        /**
         * Collect progress 1 information and update dialog UI
         */
        lifecycleScope.launch {
            viewModel.progressState1.collect { progress ->
                binding.progressBar1.animate(progress)
            }
        }

        /**
         * Collect progress 2 information and update dialog UI
         */
        lifecycleScope.launch {
            viewModel.progressState2.collect { progress ->
                binding.progressBar2.animate(progress)
            }
        }

        /**
         * Collect progress 3 information and update dialog UI
         */
        lifecycleScope.launch {
            viewModel.progressState3.collect { progress ->
                binding.progressBar3.animate(progress)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        _binding = null
    }

    private fun initUI() {
        binding.attachText1.text = getString(
            R.string.attached_solutions_text,
            viewModel.getProblemSolutionsCount(0)
        )
        binding.attachText2.text = getString(
            R.string.attached_solutions_text,
            viewModel.getProblemSolutionsCount(1)
        )
        binding.attachText3.text = getString(
            R.string.attached_solutions_text,
            viewModel.getProblemSolutionsCount(2)
        )
    }

    companion object {
        const val TAG = "Test2SubmitDialog"
        const val ANIMATION_DURATION = 2000L
    }
}