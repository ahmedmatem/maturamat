package com.ahmedmatem.android.matura

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.ahmedmatem.android.matura.ui.test.TestBottomSheetFragment

class TestActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_test)

//        val navController = findNavController(R.id.nav_host_fragment_activity_test)
        val navController = (navHostFragment as NavHostFragment).navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_test).navigateUp() ||
                super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_TEST = "test"
        const val EXTRA_TEST_ID = "test_id"
    }
}