package com.sample.makeupapp.networking

sealed class ApiResult {
    object Loading : ApiResult()
    class Failure(val e: Throwable) : ApiResult()
    class Success(val data: Any) : ApiResult()
    object Empty : ApiResult()
}