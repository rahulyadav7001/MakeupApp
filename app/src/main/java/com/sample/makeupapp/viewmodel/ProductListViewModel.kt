package com.sample.makeupapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.makeupapp.constants.Constants.MY_DEBUG_KEY
import com.sample.makeupapp.model.Product
import com.sample.makeupapp.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductListViewModel(var productRepo: ProductRepository = ProductRepository()) : ViewModel() {
    var isLoading = mutableStateOf(false)
    var productList = MutableLiveData<ArrayList<Product>>()

    // to get the Product list from the repository
    fun getProducts() {
        viewModelScope.launch {
            val data = productRepo.getProducts()
            Log.d(MY_DEBUG_KEY, "${data.size} found in response")
            data.sortBy { it.id }
            productList.value = data
        }

    }

    // to get the Product with respect to the hard coded brand from the repository
    fun getProductsByBrand() {
        val brandName = "maybelline" // kept static for testing point of view
        viewModelScope.launch {
            isLoading.value = true
            val data = productRepo.getProductsByBrand(brandName = brandName)
            Log.d(MY_DEBUG_KEY, "${data.size} found in response")
            data.sortBy { it.id }
            productList.value = data
            isLoading.value = false
        }
    }

    fun addValues(value1: Int, value2 : Int) : Int{
        return value1+value2
    }

}