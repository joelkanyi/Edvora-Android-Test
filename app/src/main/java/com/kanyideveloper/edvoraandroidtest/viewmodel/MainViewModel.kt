package com.kanyideveloper.edvoraandroidtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.edvoraandroidtest.data.MainRepository
import com.kanyideveloper.edvoraandroidtest.model.CustomProduct
import com.kanyideveloper.edvoraandroidtest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _prod = MutableLiveData<Resource<List<CustomProduct>>>()
    val prod: LiveData<Resource<List<CustomProduct>>> = _prod

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            repository.getProducts().collect { result ->
                _prod.postValue(result)
            }
        }
    }
}