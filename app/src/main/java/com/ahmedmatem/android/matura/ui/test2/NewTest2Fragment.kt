package com.ahmedmatem.android.matura.ui.test2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentNewTest2Binding
import com.ahmedmatem.android.matura.ui.test2.ProblemFragmentTab.Companion.ARG_PROBLEM_ID
import com.ahmedmatem.android.matura.ui.test2.ProblemFragmentTab.Companion.ARG_PROBLEM_NUMBER
import com.ahmedmatem.android.matura.ui.test2.ProblemFragmentTab.Companion.ARG_TEST2_ID
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewTest2Fragment : BaseFragment() {
    private val args: NewTest2FragmentArgs by navArgs()

    init {
        setHasOptionsMenu(true)
    }

    override val viewModel: NewTest2ViewModel by navGraphViewModels(R.id.nav_graph_test_2) {
        NewTest2ViewModel.Factory(args.test2Id)
    }

    private lateinit var onPageChangeCallback: OnPageChangeCallback

    private var _binding: FragmentNewTest2Binding? = null
    private val binding
        get() = _binding!!

    private lateinit var problemCollectionAdapter: ProblemCollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Register callback for listening to page changing.
         * Unregister it in onDestroyView.
         */
        onPageChangeCallback = object: OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.onProblemChanged(position + 1)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTest2Binding.inflate(inflater, container, false)

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.submit.collect { submitting ->
                    if(!submitting) return@collect
                    viewModel.submit()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.test2_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.submit_test -> {
                /** Show Submit conformation alert dialog */
                Test2SubmitConfirmDialog(/*this@NewTest2Fragment*/)
                    .show(childFragmentManager, Test2SubmitConfirmDialog.TAG)
            }
        }
        return super.onOptionsItemSelected(item)
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

class ProblemCollectionAdapter(private val baseFragment: BaseFragment)
    : FragmentStateAdapter(baseFragment) {

    private val args: NewTest2FragmentArgs by baseFragment.navArgs()

    override fun getItemCount(): Int = PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        val fragment = ProblemFragmentTab()

        val test2State = (baseFragment as NewTest2Fragment).viewModel.test2State.value
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



