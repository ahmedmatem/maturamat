package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTestBinding
import com.ahmedmatem.android.matura.ui.test.adapter.TestClickListener
import com.ahmedmatem.android.matura.ui.test.adapter.TestListAdapter

class TestListFragment : BaseFragment() {

    override lateinit var viewModel: TestListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            TestListViewModel.Factory(requireContext())
        ).get(TestListViewModel::class.java)

        // Initiate recycler adapter
        val adapter = TestListAdapter(TestClickListener {
//            Toast.makeText(requireContext(), "test: ${it.id}", Toast.LENGTH_LONG).show()
            viewModel.onTestItemClick(it)
        })

        val binding = FragmentTestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.testList.adapter = adapter

        viewModel.testList?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}