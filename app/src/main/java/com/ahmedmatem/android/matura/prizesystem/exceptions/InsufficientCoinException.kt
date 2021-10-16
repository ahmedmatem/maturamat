package com.ahmedmatem.android.matura.prizesystem.exceptions

class InsufficientCoinException : Exception {
    constructor(): super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}