package com.ahmedmatem.android.matura.ui.test2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTest2PlaceholderBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Test2PlaceholderFragment : BaseFragment() {

    override val viewModel: Test2PlaceholderViewModel by viewModels()

    private var _binding: FragmentTest2PlaceholderBinding? = null
    private val binding: FragmentTest2PlaceholderBinding get() = _binding!!

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Todo: Create test2 in placeholder fragment before navigate to new test fragment
//        viewModel.navigationCommand.value = NavigationCommand.To(
//            Test2PlaceholderFragmentDirections.actionTest2PlaceholderToNewTest2Fragment()
//        )
//
//    }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTest2PlaceholderBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startTestBtnEnabledState.collect { isEnabled ->
                    binding.startTestBtn.isEnabled = isEnabled
                    binding.testProgressBar.visibility = if(!isEnabled) View.VISIBLE else View.GONE
                }
            }
        }

        binding.startTestBtn.setOnClickListener {
            viewModel.startTest()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}