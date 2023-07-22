package com.ahmedmatem.android.matura.utils.providers

import android.content.ContentResolver
import android.content.Context

class ContentResolverProvider(val context: Context) {
    fun getContentResolver() : ContentResolver = context.contentResolver
}