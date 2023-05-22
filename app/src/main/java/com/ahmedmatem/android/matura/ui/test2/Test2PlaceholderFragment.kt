package com.ahmedmatem.android.matura.ui.test2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.base.NavigationCommand

class Test2PlaceholderFragment : BaseFragment() {

    override val viewModel: Test2PlaceholderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Todo: Create test2 in placeholder fragment before navigate to new test fragment
        viewModel.navigationCommand.value = NavigationCommand.To(
            Test2PlaceholderFragmentDirections.actionTest2PlaceholderToNewTest2Fragment()
        )

    }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test2_placeholder, container, false)
    }
}