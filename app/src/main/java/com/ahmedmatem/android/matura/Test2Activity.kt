package com.ahmedmatem.android.matura

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Test2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        /**
         * Use supportFragmentManager to find navController in case of using
         * FragmentContainerView for nav hosting
         */
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_test_2) as NavHostFragment
        val navController = navHostFragment.navController

        /**
         * Finding navController in case ot using fragment in view for nav hosting
         */
//        val navController = findNavController(R.id.nav_host_fragment_activity_test_2)

        setupActionBarWithNavController(navController, AppBarConfiguration(navController.graph))
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_test_2).navigateUp()
                || super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_TEST2_ID = "extra-test2-id"
    }
}