package com.kanyideveloper.edvoraandroidtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kanyideveloper.edvoraandroidtest.data.MainRepository
import com.kanyideveloper.edvoraandroidtest.model.Address
import com.kanyideveloper.edvoraandroidtest.model.CustomAddress
import com.kanyideveloper.edvoraandroidtest.model.CustomProduct
import com.kanyideveloper.edvoraandroidtest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _prod = MutableLiveData<Resource<List<CustomProduct>>>()
    val prod: LiveData<Resource<List<CustomProduct>>> = _prod

    private var _spinnerProducts = MutableLiveData<List<String>>()
    val spinnerProducts: LiveData<List<String>> = _spinnerProducts

    private val _spinnerStates = MutableLiveData<List<String>>()
    val spinnerStates: LiveData<List<String>> = _spinnerStates

    init {
        getProducts()
    }

    private fun getProducts(): Flow<Resource<List<CustomProduct>>> = flow {

        emit(Resource.Loading())

        try {
            val products = repository.getProducts()

            val productsList = mutableListOf("Products")
            val addressesList = mutableListOf<Address>()

            products.forEach { product ->
                productsList.add(product.productName)
                addressesList.add(product.address)
            }

            _spinnerProducts.value = productsList.distinct()


            // Grouping the addresses
            val groupedAddressesList = ArrayList<CustomAddress>()
            val groupedAddressesProducts = addressesList.groupBy {
                it.state
            }

            val statesList = mutableListOf("States")
            groupedAddressesProducts.forEach { product ->
                groupedAddressesList.add(CustomAddress(product.key, product.value))
            }




            // Grouping the products
            val groupedProductList = ArrayList<CustomProduct>()
            val groupedProducts = products.groupBy {
                it.productName
            }

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

/*    private fun getAllProducts() {
        viewModelScope.launch {
            repository.getProducts().collect { result ->
                _prod.postValue(result)

                val productsList = mutableListOf("Products")
                val statesList = mutableListOf("States")

                result.data?.forEach { customProduct ->
                    customProduct.products.forEach { product ->
                        productsList.add(product.productName)
                        statesList.add(product.address.state)
                    }
                }

                // grouping cities by state
                val groupedAddressList = ArrayList<CustomAddress>()
                val groupedAddress = result.data



                groupedProducts.forEach { product ->
                    groupedProductList.add(CustomProduct(product.key, product.value))
                }product.address.state.groupBy { state ->

                }


                _spinnerProducts.value = productsList.distinct()
                _spinnerStates.value = statesList.distinct()
            }
        }
    }*/
}