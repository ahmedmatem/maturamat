package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTestViewBinding
import com.ahmedmatem.android.matura.network.WebAppInterface
import com.ahmedmatem.android.matura.ui.general.NoticeDialogFragment
import org.koin.androidx.viewmodel.ViewModelOwner

class TestViewFragment : BaseFragment() {

    private val args: TestViewFragmentArgs by navArgs()

    override val viewModel: TestViewViewModel by viewModels(
        { requireActivity() }, // activity scope
        { TestViewViewModel.Factory(args.test) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<TestBottomSheetFragment>(R.id.bottomSheetContainer)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTestViewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding?.apply {
            testWebView.apply {
                settings.javaScriptEnabled = true
                addJavascriptInterface(WebAppInterface(requireContext()), "Android")
            }.loadUrl(viewModel.url)
        }.root
    }
}