package com.ahmedmatem.android.matura.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Switch
import androidx.preference.*
import com.ahmedmatem.android.matura.R

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Test size preference
        val testSizeKey = getString(R.string.test_size_key)
        val testSizeSeekBarPreference: SeekBarPreference? = findPreference(testSizeKey)

        // Feedback share preference
        val feedbackShareKey = getString(R.string.feedback_share_key)
        val feedbackSharePreference: Preference? = findPreference(feedbackShareKey)
        feedbackSharePreference?.setOnPreferenceClickListener { preference ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.google_play_store_url))
                type = "text/plain"
            }
            val shareIntent: Intent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
            true
        }

        // Timer preference
        setTimerPreference(testSizeSeekBarPreference?.value!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // Test size preference
        if (key == getString(R.string.test_size_key)) {
            val testSizeSeekBarPreference: SeekBarPreference? = findPreference(key)
            var newValue = testSizeSeekBarPreference?.value
            val step = resources.getInteger(R.integer.test_size_step)
            if (newValue != null) {
                while (newValue % step != 0) {
                    newValue++
                }
            }
//            Log.d("DEBUG", "onSharedPreferenceChanged: newValue = $newValue")
            sharedPreferences?.edit()?.putInt(key, newValue!!)?.apply()
            testSizeSeekBarPreference?.value =
                newValue ?: resources.getInteger(R.integer.default_test_size)

            // Switch on/off timer
            setTimerPreference(newValue!!)
        }
    }

    private fun setTimerPreference(newValue: Int) {
        val timerKey = getString(R.string.timer_key)
        val timerSwitchPreference: SwitchPreferenceCompat? = findPreference(timerKey)
        val testDefaultSize: Int = resources.getInteger(R.integer.default_test_size)
        timerSwitchPreference?.apply {
            if (newValue == testDefaultSize) {
                isEnabled = true
                summary = ""
            } else {
                isEnabled = false
                summary = getString(R.string.test_timer_off_summary, testDefaultSize)
                isChecked = false
            }
        }
    }
}