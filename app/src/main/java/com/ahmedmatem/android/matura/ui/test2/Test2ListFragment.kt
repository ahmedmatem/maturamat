package com.ahmedmatem.android.matura.ui.test2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTest2ListBinding

class Test2ListFragment : BaseFragment() {

    override lateinit var viewModel: Test2ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[Test2ListViewModel::class.java]

        val binding = FragmentTest2ListBinding.inflate(inflater, container, false)


        return binding.root
    }
}