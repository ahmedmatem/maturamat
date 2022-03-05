package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmedmatem.android.matura.utils.providers.ContextProvider
import org.koin.java.KoinJavaComponent.inject

class PrizeListener {
    private val context: Context by inject(ContextProvider::class.java)

    private val _onCoinUpdate = MutableLiveData<Boolean>()
    val onCoinUpdate: LiveData<Boolean> = _onCoinUpdate

    fun onCoinUpdate(bool: Boolean) {
        _onCoinUpdate.value = bool
    }
}