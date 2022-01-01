package com.ahmedmatem.android.matura.infrastructure

object FlavorDistribution {
    const val FREE = "free"
    const val PAID = "paid"
}

enum class ProductFlavor(flavor: String) {
    NVO_4("nvo4"),
    NVO_7("nvo7"),
    DZI_12("dzi12")
}