package com.kanyideveloper.edvoraandroidtest.data

import com.kanyideveloper.edvoraandroidtest.model.CustomProduct
import com.kanyideveloper.edvoraandroidtest.model.Product
import com.kanyideveloper.edvoraandroidtest.network.ApiService
import com.kanyideveloper.edvoraandroidtest.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getProducts(): List<Product>{
        return apiService.getProducts()
    }
}