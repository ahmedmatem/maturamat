package com.ahmedmatem.android.matura.network

enum class HttpStatus(val code: Int) {
    Ok(200),
    BadRequest(400),
    NotFound(404),
    InternalServerError(500)
}