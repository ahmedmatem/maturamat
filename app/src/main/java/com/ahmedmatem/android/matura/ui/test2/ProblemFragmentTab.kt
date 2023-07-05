package com.ahmedmatem.android.matura.ui.test2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentNewTest2TabBinding
import com.ahmedmatem.android.matura.infrastructure.MOBILE_WEB_URL
import com.ahmedmatem.android.matura.network.WebAppInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Instances in this class are fragments representing a single (problem)
 * object in collection (of problems).
 */
class ProblemFragmentTab: BaseFragment() {
    private lateinit var problemId: String
    private lateinit var problemNumber: Number
    private lateinit var test2Id: String

    override val viewModel: ProblemFragmentTabViewModel by viewModels(
        factoryProducer = {
            ProblemFragmentTabViewModel.Factory(test2Id, problemNumber as Int)
        }
    )

    private var _binding: FragmentNewTest2TabBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().apply {
            takeIf {
                it.containsKey(ARG_PROBLEM_ID).apply {
                    problemId = it.getString(ARG_PROBLEM_ID)!!
                }
            }
            takeIf {
                it.containsKey(ARG_PROBLEM_NUMBER).apply {
                    problemNumber = it.getInt(ARG_PROBLEM_NUMBER)
                }
            }
            takeIf {
                it.containsKey(ARG_TEST2_ID).apply {
                    test2Id = it.getString(ARG_TEST2_ID, null)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter = ProblemSolutionListAdapter(
            ProblemSolutionListAdapter.OnClickListener { clickedUri, currentUriList ->
                viewModel.navigateToSolutionsReviewFragment(clickedUri, currentUriList)
            })

        _binding = FragmentNewTest2TabBinding.inflate(inflater, container, false)

        /** Solution Recycler UI */
        binding.solutionsContainer.apply {
            layoutManager = GridLayoutManager(
                requireContext(),
                SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false
            )
            this.adapter = adapter
        }

        /** Solutions observer */
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getSolutions().collect {
                    viewModel.setSolutionsCount(it.count())
                    adapter.submitList(it)
                }
            }
        }

        binding.cameraButton.setOnClickListener {
            viewModel.navigateToBaseCameraFragment()
        }

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.problemWebView.apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(WebAppInterface(requireContext(), viewModel), "Android")
        }.loadUrl("$MOBILE_WEB_URL/test2/problem/$problemId")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reloadProblem.collect { reload ->
                    if(reload) {
                        binding.problemWebView.loadUrl("$MOBILE_WEB_URL/test2/problem/$problemId")
                        viewModel.onProblemReload()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.solutionCount.collect { count ->
                    binding.cameraButton.text = getString(R.string.cameraButtonText, count)
                    binding.cameraButton.isEnabled = !(count == 2)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ProblemFragmentTab"
        const val SPAN_COUNT = 2

        const val ARG_PROBLEM_NUMBER = "arg_problem_number"
        const val ARG_PROBLEM_ID = "arg_problem_id"
        const val ARG_TEST2_ID = "arg_test2_id"
    }
}