package com.sample.makeupapp.model

import com.google.gson.annotations.SerializedName

data class Product(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("brand") var brand: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("price_sign") var priceSign: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("image_link") var imageLink: String? = null,
    @SerializedName("product_link") var productLink: String? = null,
    @SerializedName("website_link") var websiteLink: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("rating") var rating: String? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("product_type") var productType: String? = null,
    @SerializedName("tag_list") var tagList: ArrayList<String> = arrayListOf(),
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("product_api_url") var productApiUrl: String? = null,
    @SerializedName("api_featured_image") var apiFeaturedImage: String? = null,
    @SerializedName("product_colors") var productColors: ArrayList<ProductColors> = arrayListOf()

)