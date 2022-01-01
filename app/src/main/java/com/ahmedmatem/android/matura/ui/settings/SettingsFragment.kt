package com.ahmedmatem.android.matura.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ahmedmatem.android.matura.R

class SettingsFragment : PreferenceFragmentCompat() {
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}