package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTestViewBinding
import com.ahmedmatem.android.matura.network.WebAppInterface
import com.ahmedmatem.lib.mathkeyboard.contracts.KeyboardExternalListener

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
        /**
         * Callback for Back pressed
         */
        requireActivity().onBackPressedDispatcher.addCallback(activity) {
            viewModel.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTestViewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.onSaveTest.observe(viewLifecycleOwner, Observer { args ->
            args?.let {
                binding.testWebView.loadUrl(
                    "javascript: saveTest(" +
                            "${args.millisInFuture}," +
                            "${args.hasTimer}," +
                            "${args.actionCode}" +
                            ")"
                )
            }
        })

        viewModel.onCheckTest.observe(viewLifecycleOwner, Observer { args ->
            args?.let {
                binding.testWebView.loadUrl(
                    "javascript: checkTest(" +
                            "${args.millisInFuture}," +
                            "${args.hasTimer}" +
                            ")"
                )
            }
        })

        viewModel.onActivityFinish.observe(viewLifecycleOwner) {
            if (it) requireActivity().finish()
        }

        return binding?.apply {
            testWebView.apply {
                settings.javaScriptEnabled = true
                addJavascriptInterface(WebAppInterface(requireContext(), viewModel), "Android")
            }.loadUrl(viewModel.url)

        }.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.test_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                viewModel.onHomePressed()
                true
            }
            R.id.check_result_menu_item -> {
                viewModel.onCheckPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

//    override fun onKeyboardCloseBtnClick() {
//        viewModel.onKeyboardClose()
//    }
//
//    override fun onKeyboardSubmit(selector: String?) {
//        TODO("Not yet implemented")
//    }
}