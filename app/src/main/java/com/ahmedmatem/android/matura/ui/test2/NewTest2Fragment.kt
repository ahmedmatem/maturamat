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
import androidx.viewpager2.adapter.FragmentStateAdapter
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

    override val viewModel: NewTest2ViewModel by viewModels()

//    private val args: NewTest2FragmentArgs by navArgs()

    private var _binding: FragmentNewTest2Binding? = null
    private val binding
        get() = _binding!!

    private lateinit var problemCollectionAdapter: ProblemCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTest2Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        problemCollectionAdapter = ProblemCollectionAdapter(this)
        binding.viewPager.apply {
            adapter = problemCollectionAdapter
        }
        // Create TabLayoutMediator to link TabLayout to ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = "Задача ${(pos+1)}"
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private const val ARG_TAB_OBJECT = "tab_object"
private const val ARG_PROBLEM_ID = "problem_id"

class ProblemCollectionAdapter(baseFragment: BaseFragment) : FragmentStateAdapter(baseFragment) {
    override fun getItemCount(): Int = PAGE_COUNT
    private val args: NewTest2FragmentArgs by baseFragment.navArgs()

    override fun createFragment(position: Int): Fragment {
        val fragment = ProblemFragmentTab()
        val problemId = when(position) {
            0 -> args.test.firstProblemId
            1 -> args.test.secondProblemId
            2 -> args.test.thirdProblemId
            else ->
                Log.e(TAG, "createFragment: Pages($position) must be less than $PAGE_COUNT", )
        }
        fragment.arguments = bundleOf(
            Pair(ARG_TAB_OBJECT, position + 1),
            Pair(ARG_PROBLEM_ID, problemId),
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
    override val viewModel: NewTest2ViewModel by viewModels({requireParentFragment()})
    private var _binding: FragmentNewTest2TabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTest2TabBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { bundle ->
            bundle.containsKey(ARG_TAB_OBJECT).apply {
                binding.problemNoText.text = getString(R.string.problem_no, bundle.getInt(ARG_TAB_OBJECT))
                val problemId = bundle.getString(ARG_PROBLEM_ID)
                binding.problemWebView.apply {
                    settings.javaScriptEnabled = true
                    addJavascriptInterface(WebAppInterface(requireContext(), viewModel), "Android")
                }.loadUrl("$MOBILE_WEB_URL/test2/problem/$problemId")
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.unloadProblemId.collect { id ->
                    id?.let {
                        binding.problemWebView.loadUrl("$MOBILE_WEB_URL/test2/problem/$it")
                    }
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