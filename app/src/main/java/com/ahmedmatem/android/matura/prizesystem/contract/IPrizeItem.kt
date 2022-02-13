package com.ahmedmatem.android.matura.prizesystem.contract

interface IPrizeItem {
    val holder: String
    // Amount of prize given from app as a gift (for some period of time or endless)
    var gift: Int
    // Amount of prize earned bu user (or guest) by different app activities
    var earned: Int
    // gift + earned
    val total: Int
    // picture of prize
    var drawableResId: Int
    var synced: Boolean

    fun bet(amount: Int = 1)
    fun add(amount: Int = 1)
    fun reset()
}