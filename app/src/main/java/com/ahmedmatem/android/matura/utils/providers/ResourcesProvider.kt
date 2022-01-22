package com.ahmedmatem.android.matura.utils.providers

import android.content.Context
import android.content.res.Resources

class ResourcesProvider(val context: Context) {
    fun getResources(): Resources {
        return context.resources
    }
}