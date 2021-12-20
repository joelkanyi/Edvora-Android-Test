package com.kanyideveloper.edvoraandroidtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.edvoraandroidtest.data.MainRepository
import com.kanyideveloper.edvoraandroidtest.model.Address
import com.kanyideveloper.edvoraandroidtest.model.CustomAddress
import com.kanyideveloper.edvoraandroidtest.model.CustomProduct
import com.kanyideveloper.edvoraandroidtest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    // List of All Products
    private val _prod = MutableLiveData<Resource<List<CustomProduct>?>>()
    val prod: LiveData<Resource<List<CustomProduct>?>> = _prod

    // List of Products Names - Spinner Items
    private var _spinnerProducts = MutableLiveData<List<String>>()
    val spinnerProducts: LiveData<List<String>> = _spinnerProducts

    // List of States Names - Spinner Items
    private val _spinnerStates = MutableLiveData<List<String>>()
    val spinnerStates: LiveData<List<String>> = _spinnerStates

    // List of Cities Names - Spinner Items
    private val _spinnerCities = MutableLiveData<List<String>>()
    val spinnerCities: LiveData<List<String>> = _spinnerCities

    // List of All Addresses
    private val addresses = mutableListOf<Address>()

    // List of Custom Products
    private var products: List<CustomProduct>? = emptyList()

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            repository.getProducts().collect { result ->
                _prod.postValue(result)

                val productsList = mutableListOf("Products")
                val statesList = mutableListOf("States")

                products = result.data

                result.data?.forEach { customProduct ->
                    customProduct.products.forEach { product ->
                        productsList.add(product.productName)
                        statesList.add(product.address.state)
                        addresses.add(product.address)
                    }
                }

                _spinnerProducts.value = productsList.distinct()
                _spinnerStates.value = statesList.distinct()
            }
        }
    }

    fun citiesSpinner(state: String?) {
        val citiesList = mutableListOf("City")
        val groupedAddressesList = ArrayList<CustomAddress>()

        val groupedStates = addresses.groupBy {
            it.state
        }

        groupedStates.forEach { address ->
            groupedAddressesList.add(CustomAddress(address.key, address.value))
        }

        groupedAddressesList.forEach { customAddress ->
            if (customAddress.stateName == state) {
                customAddress.cities.forEach { city ->
                    citiesList.add(city.city)
                }
            }
        }

        _spinnerCities.value = citiesList.distinct()
    }

    fun filterProducts(productName: String?) {
        Timber.d("ProductName: $productName")

        if (productName != "Products"){
            val filteredProducts = products?.filter {
                it.productName == productName
            }

            _prod.value = Resource.Success(filteredProducts)
        }else{
            return
        }
    }

    fun clearFilter() {
        _prod.value = Resource.Success(products)
    }
}