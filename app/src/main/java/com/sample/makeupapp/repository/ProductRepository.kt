package com.sample.makeupapp.repository

import RetrofitClient
import com.sample.makeupapp.networking.ApiResult
import java.io.IOException

class ProductRepository {
    private val productServiceApiInterface = RetrofitClient.productServiceApi

    // to get the Product list from the Server
    suspend fun getProducts(): ApiResult {
        return try {
            val response = productServiceApiInterface.getProducts()
            ApiResult.Success(response)
        } catch (e: IOException) {
            ApiResult.Error(e)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }

    }

    // to get the Product with respect to the hard coded brand from the server
    suspend fun getProductsByBrand(brandName: String): ApiResult {
        return try {
            val response = productServiceApiInterface.getProductsByBrand(brandName = brandName)
            ApiResult.Success(response)
        } catch (e: IOException) {
            ApiResult.Error(e)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}