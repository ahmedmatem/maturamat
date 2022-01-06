package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.base.hideBottomNavigation
import com.ahmedmatem.android.matura.databinding.FragmentNewTestBinding

class NewTestFragment : BaseFragment() {
    override lateinit var viewModel: NewTestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewTestViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewTestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        hideBottomNavigation(R.id.nav_view)

        return binding.root
    }
}