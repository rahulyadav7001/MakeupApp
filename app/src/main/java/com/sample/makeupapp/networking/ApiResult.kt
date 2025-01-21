package com.sample.makeupapp.networking

sealed class ApiResult {
    class Error(val e: Throwable) : ApiResult()
    class Success(val data: Any) : ApiResult()
}