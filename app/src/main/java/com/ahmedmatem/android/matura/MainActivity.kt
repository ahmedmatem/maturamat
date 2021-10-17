package com.ahmedmatem.android.matura

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.ahmedmatem.android.matura.databinding.ActivityMainBinding
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.worker.SetupPrizeOnAppStartWorker
import com.ahmedmatem.android.matura.prizesystem.worker.SetupPrizeOnLoginWorker

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Only for free distribution in all app versions setup Prize onAppStart
         */
        if (BuildConfig.FLAVOR_distribution == "free" &&
            UserPrefs(applicationContext).getUser() != null
        ) {
            val setupPrizeOnAppStartRequest =
                OneTimeWorkRequest.from(SetupPrizeOnAppStartWorker::class.java)
            WorkManager.getInstance(applicationContext).enqueue(setupPrizeOnAppStartRequest)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_tests,
                R.id.navigation_account,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}