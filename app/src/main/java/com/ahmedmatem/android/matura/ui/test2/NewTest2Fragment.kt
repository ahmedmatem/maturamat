package com.ahmedmatem.android.matura.ui.test2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentNewTest2Binding
import com.ahmedmatem.android.matura.databinding.FragmentNewTest2TabObjectBinding

class NewTest2Fragment : BaseFragment() {

    override val viewModel: NewTest2ViewModel by viewModels()

    private var _binding: FragmentNewTest2Binding? = null
    private val binding = _binding!!

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
        val problemViewPager = binding.problemViewPager
        problemViewPager.adapter = problemCollectionAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private const val ARG_TAB_OBJECT = "tab_object"

class ProblemCollectionAdapter(baseFragment: BaseFragment) : FragmentStateAdapter(baseFragment) {
    override fun getItemCount(): Int = PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        val fragment = ProblemFragmentTab()
        fragment.arguments = bundleOf(
            Pair(ARG_TAB_OBJECT, position + 1)
        )
        return fragment
    }

    companion object {
        const val PAGE_COUNT: Int = 3
    }
}

/**
 * Instances in this class are fragments representing a single (problem)
 * object in collection (of problems).
 */
class ProblemFragmentTab: Fragment() {
    private var _binding: FragmentNewTest2TabObjectBinding? = null
    private val binding: FragmentNewTest2TabObjectBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewTest2TabObjectBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_new_test2_tab_object, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { bundle ->
            bundle.containsKey(ARG_TAB_OBJECT).apply {
                binding.tabName.text = "Tab ${bundle.getInt(ARG_TAB_OBJECT)}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}