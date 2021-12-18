package com.kanyideveloper.edvoraandroidtest.model

import com.google.gson.annotations.SerializedName

data class Product(
    val address: Address,
    @SerializedName("brand_name")
    val brandName: String,
    val date: String,
    @SerializedName("discription")
    val description: String,
    val image: String,
    val price: Int,
    @SerializedName("product_name")
    val productName: String,
    val time: String
)