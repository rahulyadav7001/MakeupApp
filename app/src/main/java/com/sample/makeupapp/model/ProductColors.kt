package com.sample.makeupapp.model


import com.google.gson.annotations.SerializedName


data class ProductColors(

    @SerializedName("hex_value") var hexValue: String? = null,
    @SerializedName("colour_name") var colourName: String? = null

)