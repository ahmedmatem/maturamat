package com.ahmedmatem.android.matura.ui.test2

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentNewTest2Binding
import com.ahmedmatem.android.matura.databinding.FragmentNewTest2TabBinding
import com.ahmedmatem.android.matura.infrastructure.MOBILE_WEB_URL
import com.ahmedmatem.android.matura.network.WebAppInterface
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewTest2Fragment : BaseFragment() {
    private val args: NewTest2FragmentArgs by navArgs()

    override val viewModel: NewTest2ViewModel by navGraphViewModels(R.id.nav_graph_test_2) {
        NewTest2ViewModel.Factory(args.test2Id)
    }

    private lateinit var onPageChangeCallback: OnPageChangeCallback

    private var _binding: FragmentNewTest2Binding? = null
    private val binding
        get() = _binding!!

    private lateinit var problemCollectionAdapter: ProblemCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTest2Binding.inflate(inflater, container, false)

        /**
         * Register callback for listening to page changing.
         * Unregister it in onDestroyView.
         */
        onPageChangeCallback = object: OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.onProblemChanged(position + 1)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /** Observe Test2 initialization and when it is done initialize pageViewer. */
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onTest2Initialized.collect { isTest2Initialized ->
                    if(isTest2Initialized) {
                        problemCollectionAdapter = ProblemCollectionAdapter(this@NewTest2Fragment)
                        binding.viewPager.apply {
                            adapter = problemCollectionAdapter
                            registerOnPageChangeCallback(onPageChangeCallback)
                        }
                        // Create TabLayoutMediator to link TabLayout to ViewPager
                        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
                            tab.text = "Задача ${(pos + 1)}"
                        }.attach()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /** Unregister onPageChangedCallback */
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        _binding = null
    }

    companion object {
        const val TAG = "NewTest2Fragment"
    }
}

private const val ARG_PROBLEM_NUMBER = "arg_problem_number"
private const val ARG_PROBLEM_ID = "arg_problem_id"
private const val ARG_TEST2_ID = "arg_test2_id"

class ProblemCollectionAdapter(private val containerFragment: NewTest2Fragment)
    : FragmentStateAdapter(containerFragment) {

    private val args: NewTest2FragmentArgs by containerFragment.navArgs()

    override fun getItemCount(): Int = PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        val fragment = ProblemFragmentTab()

        val test2State = containerFragment.viewModel.test2State.value
        val problemId = when(position) {
            0 -> test2State?.firstProblemId
            1 -> test2State?.secondProblemId
            2 -> test2State?.thirdProblemId
            else ->
                Log.e(TAG, "createFragment: Pages($position) must be less than $PAGE_COUNT")
        }
        // Set fragment arguments
        fragment.arguments = bundleOf(
            Pair(ARG_PROBLEM_NUMBER, position + 1),
            Pair(ARG_PROBLEM_ID, problemId),
            Pair(ARG_TEST2_ID, args.test2Id)
        )

        return fragment
    }

    companion object {
        const val PAGE_COUNT: Int = 3
        const val TAG = "ProblemCollAdapter"
    }
}

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.solutionListState.collect {

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter = SolutionListAdapter(SolutionListAdapter.OnClickListener { uri: String ->
            // todo: not implemented yet
        })

        _binding = FragmentNewTest2TabBinding.inflate(inflater, container, false)

        /** Solution Recycler UI */
        binding.solutionsContainer.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            this.adapter = adapter
        }

        /** Solutions observer */
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getSolutions().collect {
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
                viewModel.solutionCount.collect {count ->
                    binding.cameraButton.text = getString(R.string.cameraButtonText, count)
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
    }
}