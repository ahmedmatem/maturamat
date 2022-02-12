package com.ahmedmatem.android.matura.ui.test

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.TestActivity
import com.ahmedmatem.android.matura.TestActivity.Companion.EXTRA_TEST
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTestListBinding
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.ui.test.adapter.TestClickListener
import com.ahmedmatem.android.matura.ui.test.adapter.TestListAdapter

class TestListFragment : BaseFragment() {

    override lateinit var viewModel: TestListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(TestListViewModel::class.java)

        // Initiate recycler adapter
        val adapter = TestListAdapter(TestClickListener {
            viewModel.onTestItemClick(it)
        })

        val binding = FragmentTestListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.testList.adapter = adapter

        binding.testStartFab.setOnClickListener(View.OnClickListener {
            Intent(requireContext(), TestActivity::class.java).apply {
                val test: Test? = null // put null value for test in extra
                putExtra(EXTRA_TEST, test)
                startActivity(this)
            }
        })

        /**
         * Observe Fab button in order to show or hide it
         * depending of user prize in free app distribution.
         */
        viewModel.isFabVisible.observe(viewLifecycleOwner) { visible ->
            binding.isFabVisible = visible
        }

        viewModel.testList?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.onTestItemClick.observe(viewLifecycleOwner, Observer { test ->
            test?.let {
                // Create intent with extra containing test
                Intent(requireContext(), TestActivity::class.java).apply {
                    putExtra(EXTRA_TEST, it)
                    startActivity(this)
                }
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshTestList()
    }
}