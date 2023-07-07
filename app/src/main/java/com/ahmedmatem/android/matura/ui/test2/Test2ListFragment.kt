package com.ahmedmatem.android.matura.ui.test2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedmatem.android.matura.Test2Activity
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTest2ListBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Test2ListFragment : BaseFragment() {

    override lateinit var viewModel: Test2ListViewModel

    private var _binding: FragmentTest2ListBinding? = null
    private val binding: FragmentTest2ListBinding get() = _binding!!

    private val test2ActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            it.data?.let {
//                if (data.hasExtra(TestActivity.EXTRA_TEST_ID)) {
//                    val testId = data.getStringExtra(TestActivity.EXTRA_TEST_ID)
//                    testId?.let { id ->
//                        viewModel.refreshTestById(id)
//                    } ?: run {
//                        viewModel.refreshLastTest()
//                    }
//                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[Test2ListViewModel::class.java]

        val adapter = Test2ListAdapter(Test2ListAdapter.OnClickListener {
            // todo: Implement click listener
        })

        _binding = FragmentTest2ListBinding.inflate(inflater, container, false)

        binding.testListContainer.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        lifecycleScope.launch {
            viewModel.getAll().collect {
                adapter.submitList(it)
            }
        }

        binding.createNewTest2.setOnClickListener {
            val intent = Intent(requireContext(), Test2Activity::class.java).apply {
                putExtras(bundleOf(
                    // Pair()
                ))
            }
            test2ActivityLauncher.launch(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}