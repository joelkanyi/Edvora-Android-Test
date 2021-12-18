package com.kanyideveloper.edvoraandroidtest.network

import com.kanyideveloper.edvoraandroidtest.model.Product
import retrofit2.http.GET

interface ApiService {

    @GET("/")
    suspend fun getProducts() : List<Product>

    companion object{
        const val BASE_URL = "https://assessment-edvora.herokuapp.com/"
    }
}