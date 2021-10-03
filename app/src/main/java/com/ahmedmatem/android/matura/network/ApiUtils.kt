package com.ahmedmatem.android.matura.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import retrofit2.HttpException

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): Result<T> {
    return withContext(dispatcher) {
        try {
            Result.Success(apiCall.invoke())
        } catch (exc: Exception) {
            when (exc) {
                is IOException -> Result.NetworkError
                is HttpException -> {
                    val code = exc.code()
                    val errorResponse = convertErrorBody(exc)
                    Result.GenericError(code, errorResponse)
                }
                else -> Result.GenericError()
            }
        }
    }

}

fun convertErrorBody(httpExc: HttpException): ErrorResponse? {
    return try {
        httpExc.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Retrofit.moshi.adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exc: Exception) {
        null
    }
}
