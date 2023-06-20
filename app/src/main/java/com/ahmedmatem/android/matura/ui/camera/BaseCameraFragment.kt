package com.ahmedmatem.android.matura.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment

class BaseCameraFragment : BaseFragment() {

    override val viewModel: BaseCameraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_camera, container, false)
    }
}