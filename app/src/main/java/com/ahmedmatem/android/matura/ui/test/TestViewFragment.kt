package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTestViewBinding
import com.ahmedmatem.android.matura.network.WebAppInterface

class TestViewFragment : BaseFragment() {

    private val args: TestViewFragmentArgs by navArgs()

    override val viewModel: TestViewViewModel by viewModels(
        { requireActivity() }, // activity scope
        { TestViewViewModel.Factory(args.test) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<TestBottomSheetFragment>(R.id.bottomSheetContainer)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(activity) {
            viewModel.onBackPressed()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.test_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}