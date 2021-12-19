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
import android.view.View

import androidx.core.content.ContextCompat
import com.kanyideveloper.edvoraandroidtest.R


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

        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.main_dark)

        // set status background black
        val decorView: View = window.decorView

        //set status text  light
        decorView.systemUiVisibility =
            decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()

        setUpObserver()

        binding.filters.setOnClickListener {
            binding.filterCard.isVisible = !binding.filterCard.isVisible
        }
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