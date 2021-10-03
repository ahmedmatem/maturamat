package com.ahmedmatem.android.matura.network

import java.lang.Exception

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()

    data class GenericError(val code: Int? = null, val errorResponse: ErrorResponse? = null) :
        Result<Nothing>()

    object NetworkError: Result<Nothing>()
}