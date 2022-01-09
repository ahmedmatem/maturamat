package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment

class TestBottomSheetFragment: BaseFragment() {
    override val viewModel: TestBottomSheetViewModel
        get() = ViewModelProvider(this).get(TestBottomSheetViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.test_list_bottom_sheet, container, false)
        return view
    }
}