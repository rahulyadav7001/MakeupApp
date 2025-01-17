package com.sample.makeupapp.repository

import RetrofitClient
import com.sample.makeupapp.model.Product

class ProductRepository {
    private val productServiceApiInterface = RetrofitClient.productServiceApi

    // to get the Product list from the Server
    suspend fun getProducts(): ArrayList<Product> {
        return productServiceApiInterface.getProducts()
    }

    // to get the Product with respect to the hard coded brand from the server
    suspend fun getProductsByBrand(brandName: String): ArrayList<Product> {
        return productServiceApiInterface.getProductsByBrand(brandName = brandName)
    }
}