package com.ahmedmatem.android.matura.utils

import android.content.Context
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.infrastructure.ProductFlavor

class URLUtil private constructor(ctx: Context, flavor: ProductFlavor) {
    private val _context: Context = ctx
    private val _productFlavor: ProductFlavor = flavor

    fun newTestUrl(): String {
        val baseTestUrl: String = _context.getString(R.string.base_test_url)
        var newTestUrl: StringBuilder = StringBuilder(baseTestUrl)
        

        return newTestUrl()
    }

    companion object {
        fun from(context: Context, flavor: ProductFlavor): URLUtil {
            val productFlavor = when (flavor) {
                ProductFlavor.NVO_4 -> ProductFlavor.NVO_4
                ProductFlavor.NVO_7 -> ProductFlavor.NVO_7
                ProductFlavor.DZI_12 -> ProductFlavor.DZI_12
            }
            return URLUtil(context, productFlavor)
        }
    }
}