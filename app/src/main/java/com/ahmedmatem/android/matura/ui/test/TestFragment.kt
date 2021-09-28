package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.databinding.FragmentTestBinding
import com.ahmedmatem.android.matura.ui.test.adapter.TestClickListener
import com.ahmedmatem.android.matura.ui.test.adapter.TestListAdapter

class TestFragment : Fragment() {

    private lateinit var viewModel: TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(TestViewModel::class.java)

        val binding = FragmentTestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Initiate recycler adapter
        val adapter = TestListAdapter(TestClickListener {

        })

        binding.testList.adapter = adapter

        return binding.root
    }
}