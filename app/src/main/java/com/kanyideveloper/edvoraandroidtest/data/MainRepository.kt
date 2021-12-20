package com.kanyideveloper.edvoraandroidtest.data

import com.kanyideveloper.edvoraandroidtest.model.CustomProduct
import com.kanyideveloper.edvoraandroidtest.network.ApiService
import com.kanyideveloper.edvoraandroidtest.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService) {

    fun getProducts(): Flow<Resource<List<CustomProduct>?>> = flow {

        emit(Resource.Loading())

        try {

            // Making the API Call
            val products = apiService.getProducts()

            // Grouping the products
            val groupedProductList = ArrayList<CustomProduct>()
            val groupedProducts = products.groupBy {
                it.productName
            }

            // Adding the grouped products into a list
            groupedProducts.forEach { product ->
                groupedProductList.add(CustomProduct(product.key, product.value))
            }

            // Emit our data to the UI
            emit(Resource.Success(groupedProductList))

        } catch (e: HttpException) {
            emit(
                Resource.Failure(message = "Oops, something went wrong!")
            )

        } catch (e: IOException) {
            emit(
                Resource.Failure(message = "Couldn't reach server, check your internet connection!")
            )
        }
    }
}