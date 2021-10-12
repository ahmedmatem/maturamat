package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTestBinding
import com.ahmedmatem.android.matura.ui.test.adapter.TestClickListener
import com.ahmedmatem.android.matura.ui.test.adapter.TestListAdapter
import java.util.*

class TestFragment : BaseFragment() {

    override lateinit var viewModel: TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            TestViewModel.Factory(requireContext())
        ).get(TestViewModel::class.java)

        Log.d("TAG", "testList = ${viewModel.testList.value?.size}")

        val binding = FragmentTestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Initiate recycler adapter
        val adapter = TestListAdapter(TestClickListener {

        })

        binding.testList.adapter = adapter

        viewModel.testList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}