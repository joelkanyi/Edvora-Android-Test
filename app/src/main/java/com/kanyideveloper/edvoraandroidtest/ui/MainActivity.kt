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
import android.widget.AdapterView

import androidx.core.content.ContextCompat
import com.kanyideveloper.edvoraandroidtest.R
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import timber.log.Timber


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

        // Make the statusbar dark and make the text white
        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.main_dark)
        val decorView: View = window.decorView
        decorView.systemUiVisibility =
            decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()

        // Invoke the observer
        setUpObserver()

        // Swipe to Refresh
        binding.swipeLayout.setOnRefreshListener {
            viewModel.getAllProducts()
        }

        // Apply Filter
        binding.filters.setOnClickListener {
            binding.filterCard.isVisible = !binding.filterCard.isVisible
        }

        // Clear Filter
        binding.textViewClearFilter.setOnClickListener {
            viewModel.clearFilter()
            binding.filterCard.isVisible = false
        }

        // Products Spinner
        viewModel.spinnerProducts.observe(this, Observer { products ->
            Timber.d(products.toString())
            val arrayAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, products)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.productsSpinner.adapter = arrayAdapter

        })

        // States Spinner
        viewModel.spinnerStates.observe(this, Observer { states ->
            Timber.d(states.toString())
            val arrayAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.statesSpinner.adapter = arrayAdapter
        })

        // Cities Spinner
        viewModel.spinnerCities.observe(this, Observer { cities ->
            Timber.d(cities.toString())
            val arrayAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.citiesSpinner.adapter = arrayAdapter
        })

        // Listen for selected state so as to update the Cities Spinner accordingly
        binding.statesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.citiesSpinner(binding.statesSpinner.selectedItem.toString())
                if (binding.statesSpinner.selectedItem.toString() != "States") {
                    binding.filterCard.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }


        // Filter Products
        binding.productsSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.filterProducts(binding.productsSpinner.selectedItem.toString())
                    if (binding.productsSpinner.selectedItem.toString() != "Products") {
                        binding.filterCard.isVisible = false
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.citiesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (binding.citiesSpinner.selectedItem.toString() != "City") {
                    binding.filterCard.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setUpObserver() {
        viewModel.prod.observe(this, { result ->
            when (result) {
                is Resource.Success -> {
                    binding.productsProgressBar.isVisible = false
                    binding.swipeLayout.isRefreshing = false
                    binding.imageViewMessage.isVisible = false
                    binding.textViewMessage.isVisible = false

                    binding.allProductsRecycler.isVisible = true

                    if (result.data?.isEmpty()!!) {
                        binding.imageViewMessage.setImageResource(R.drawable.ic_empty_state)
                        binding.textViewMessage.text = "Nothing Found!"
                        binding.imageViewMessage.isVisible = true
                        binding.textViewMessage.isVisible = true

                        binding.allProductsRecycler.isVisible = false

                    } else {
                        allProductsAdapter.submitList(result.data)
                        binding.allProductsRecycler.adapter = allProductsAdapter
                    }
                }

                is Resource.Failure -> {
                    binding.productsProgressBar.isVisible = false
                    binding.swipeLayout.isRefreshing = false

                    binding.imageViewMessage.setImageResource(R.drawable.ic_error)
                    binding.textViewMessage.text = result.message
                    binding.imageViewMessage.isVisible = true
                    binding.textViewMessage.isVisible = true
                }

                is Resource.Loading -> {
                    binding.productsProgressBar.isVisible = true
                    binding.imageViewMessage.isVisible = false
                    binding.textViewMessage.isVisible = false
                }
            }
        })
    }
}