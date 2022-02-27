package com.ahmedmatem.android.matura.ui.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.TestActivity
import com.ahmedmatem.android.matura.TestActivity.Companion.EXTRA_TEST
import com.ahmedmatem.android.matura.TestActivity.Companion.EXTRA_TEST_ID
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTestListBinding
import com.ahmedmatem.android.matura.infrastructure.FlavorDistribution
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.ui.test.adapter.TestClickListener
import com.ahmedmatem.android.matura.ui.test.adapter.TestListAdapter
import org.koin.java.KoinJavaComponent.inject

class TestListFragment : BaseFragment() {

    override lateinit var viewModel: TestListViewModel

    private val userPrefs: UserPrefs by inject(UserPrefs::class.java)

    /**
     * TestActivity launcher
     * Launcher callback must receive testId in order to populate it in local db.
     * If it is null just get and populate last test.
     */
    private val testActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.let { data ->
                    if (data.hasExtra(EXTRA_TEST_ID)) {
                        val testId = data.getStringExtra(EXTRA_TEST_ID)
                        testId?.let { id ->
                            viewModel.refreshTestById(id)
                        } ?: run {
                            viewModel.refreshLastTest()
                        }
                    }
                }
            }
        }

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
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.testList.adapter = adapter

        binding.testStartFab.setOnClickListener(View.OnClickListener {
            if (BuildConfig.FLAVOR_distribution == FlavorDistribution.FREE) {
                viewModel.bet()
            }
            // Start test Activity for result.
            Intent(requireContext(), TestActivity::class.java).apply {
                val test: Test? = null // put null value for test in extra
                putExtra(EXTRA_TEST, test)
                testActivityResultLauncher.launch(this)
            }
        })

        viewModel.testList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.onTestItemClick.observe(viewLifecycleOwner, Observer { test ->
            test?.let {
                // Create intent with extra containing test.
                // Start test Activity for result.
                Intent(requireContext(), TestActivity::class.java).apply {
                    putExtra(EXTRA_TEST, it)
                    testActivityResultLauncher.launch(this)
                }
            }
        })

        return binding.root
    }

    /*override fun onResume() {
        super.onResume()
        viewModel.refreshLastTest()
    }*/

    companion object {
        const val TAG = "TestListFragment"
    }
}