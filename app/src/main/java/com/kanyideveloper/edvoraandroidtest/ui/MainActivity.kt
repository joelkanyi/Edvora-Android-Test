package com.kanyideveloper.edvoraandroidtest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.kanyideveloper.edvoraandroidtest.adapters.AllProductsAdapter
import com.kanyideveloper.edvoraandroidtest.databinding.ActivityMainBinding
import com.kanyideveloper.edvoraandroidtest.util.Resource
import com.kanyideveloper.edvoraandroidtest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val allProductsAdapter by lazy {
        AllProductsAdapter()
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.prod.observe(this, { result ->
            when (result) {
                is Resource.Success -> {
                    binding.productsProgressBar.isVisible = false

                    if (result.data?.isEmpty()!!) {
                        //showSnackbar("Nothing Found, Try Again!")
                    } else {
                        allProductsAdapter.submitList(result.data)
                        binding.allProductsRecycler.adapter = allProductsAdapter
                    }
                }

                is Resource.Failure -> {
                    binding.productsProgressBar.isVisible = false
                    //showSnackbar(result.message ?: "Unknown Error!")
                }

                is Resource.Loading -> {
                    binding.productsProgressBar.isVisible = true
                }
            }
        })
    }
}