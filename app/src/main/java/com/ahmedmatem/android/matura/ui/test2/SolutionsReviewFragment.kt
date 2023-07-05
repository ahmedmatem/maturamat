package com.ahmedmatem.android.matura.ui.test2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentSolutionsReviewBinding
import com.ahmedmatem.android.matura.utils.clearFullScreen
import com.ahmedmatem.android.matura.utils.setFullScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SolutionsReviewFragment : BaseFragment() {

    override val viewModel: SolutionsReviewViewModel by viewModels()

    private val args: SolutionsReviewFragmentArgs by navArgs()

    private var _binding: FragmentSolutionsReviewBinding? = null
    private val binding: FragmentSolutionsReviewBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolutionsReviewBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.zoomScaleState.collect { scale ->
                    binding.pager.isUserInputEnabled = scale == 1f
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFullScreen()
        val solutionCollectionAdapter = SolutionsCollectionAdapter(this)
        binding.pager.apply {
            adapter = solutionCollectionAdapter
            post {
                currentItem = args.solutionReviewArgs.clickedUriIndex
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        clearFullScreen()
    }
}

class SolutionsCollectionAdapter(baseFragment: BaseFragment)
    : FragmentStateAdapter(baseFragment) {

    private val args: SolutionsReviewFragmentArgs by baseFragment.navArgs()

    override fun getItemCount(): Int = args.solutionReviewArgs.uris.size

    override fun createFragment(position: Int) =
        SolutionViewFragment.newInstance(args.solutionReviewArgs.uris[position])
}