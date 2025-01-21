package com.sample.makeupapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.makeupapp.constants.Constants.MY_DEBUG_KEY
import com.sample.makeupapp.model.Product
import com.sample.makeupapp.networking.ApiResult
import com.sample.makeupapp.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductListViewModel(var productRepo: ProductRepository = ProductRepository()) : ViewModel() {
    var isLoading = mutableStateOf(false)
    var isError = mutableStateOf(false)
    var errorMessage = mutableStateOf("")
    var productList = MutableLiveData<ArrayList<Product>>()

    // to get the Product list from the repository
    fun getProducts() {
        viewModelScope.launch {
            isLoading.value = true
            isError.value = false
            when (val apiResult = productRepo.getProducts()) {
                is ApiResult.Success -> {
                    val data = apiResult.data as ArrayList<Product>
                    Log.d(MY_DEBUG_KEY, "${data.size} found in response")
                    data.sortBy { it.id }
                    productList.value = data
                    isLoading.value = false
                    isError.value = false
                }

                is ApiResult.Error -> {
                    Log.d(MY_DEBUG_KEY, "Error found - ${apiResult.e.message}")
                    showError(apiResult)
                }

                else -> {
                    Log.d(MY_DEBUG_KEY, "Data Loading...")
                    isLoading.value = true
                    isError.value = false
                }
            }
        }
    }

    // to get the Product with respect to the hard coded brand from the repository
    fun getProductsByBrand() {
        val brandName = "maybelline" // kept static for testing point of view
        viewModelScope.launch {
            isLoading.value = true
            isError.value = false
            when (val apiResult = productRepo.getProductsByBrand(brandName = brandName)) {
                is ApiResult.Success -> {
                    val data = apiResult.data as ArrayList<Product>
                    Log.d(MY_DEBUG_KEY, "${data.size} found in response")
                    data.sortBy { it.id }
                    productList.value = data
                    isLoading.value = false
                    isError.value = false
                }

                is ApiResult.Error -> {
                    Log.d(MY_DEBUG_KEY, "Error found - ${apiResult.e.message}")
                    showError(apiResult)
                }

                else -> {
                    Log.d(MY_DEBUG_KEY, "Data Loading...")

                    isLoading.value = true
                    isError.value = false
                }

            }

        }
    }

    private fun showError(apiResult: ApiResult.Error) {
        isLoading.value = false
        isError.value = true
        errorMessage.value = "${apiResult.e.message} "
    }

}