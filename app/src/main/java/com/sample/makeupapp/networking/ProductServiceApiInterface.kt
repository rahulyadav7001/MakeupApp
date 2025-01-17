package com.sample.makeupapp.networking


import com.sample.makeupapp.model.Product
import com.sample.makeupapp.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductServiceApiInterface {

    @GET("products.json")
    suspend fun getProducts(): ArrayList<Product>

    @GET("products.json")
    suspend fun getProductsByBrand(@Query(value = "brand") brandName : String): ArrayList<Product>
}